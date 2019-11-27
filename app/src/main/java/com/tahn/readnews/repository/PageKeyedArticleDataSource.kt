package com.tahn.readnews.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tahn.readnews.api.NewsApiService
import com.tahn.readnews.data.NetworkState
import com.tahn.readnews.data.Article
import java.util.concurrent.Executor

class PageKeyedArticleDataSource(
    private val api : NewsApiService,
    private val country : String,
    private val category : String,
    private val retryExecutor : Executor
) : PageKeyedDataSource<Int, Article>() {

    var retry :(() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        val currentPage = 1
        val nextPage = currentPage + 1

        createLoadInit(params, callback, currentPage, nextPage)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        val currentPage = params.key
        val nextPage = currentPage + 1

        createLoadAfter(params, callback, currentPage, nextPage)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {}

    private fun createLoadInit(
        params : LoadInitialParams<Int>,
        callback : LoadInitialCallback<Int, Article>,
        currentPage : Int,
        nextPage : Int
    ){
        api.getArticlesSync(
            country = country,
            category = category,
            page = currentPage,
            perPage = params.requestedLoadSize,
            onPrepared = {
                postInitialState(NetworkState.LOADING)
            },
            onSuccess = {response ->
                val items = response?.data ?: emptyList()
                retry = null
                postInitialState(NetworkState.LOADED)
                callback.onResult(items, null, nextPage)
            },
            onError = {errorMessage ->
                retry ={loadInitial(params, callback)}
                postInitialState(NetworkState.error(errorMessage))

            })
    }

    private fun createLoadAfter(
        params : LoadParams<Int>,
        callback : LoadCallback<Int, Article>,
        currentPage : Int,
        nextPage : Int
    ){
        api.getArticlesSync(
            country = country,
            category = category,
            page = currentPage,
            perPage = params.requestedLoadSize,
            onPrepared = {
                postInitialState(NetworkState.LOADING)
            },
            onSuccess = {response ->
                val items = response?.data ?: emptyList()
                retry = null
                postInitialState(NetworkState.LOADED)
                callback.onResult(items, nextPage)
            },
            onError = {errorMessage ->
                retry ={ loadAfter(params, callback) }
                postInitialState(NetworkState.error(errorMessage))
            })
    }

    fun retryAllFailed(){
        val prevRetry = retry
        retry = null
        prevRetry?.let { retry ->
            retryExecutor.execute { retry() }
        }
    }

    private fun postInitialState(state: NetworkState) {
        networkState.postValue(state)
        initialLoad.postValue(state)
    }

    private fun postAfterState(state: NetworkState) {
        networkState.postValue(state)
    }
}