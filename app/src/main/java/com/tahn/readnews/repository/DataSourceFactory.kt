package com.tahn.readnews.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tahn.readnews.api.NewsApiService
import com.tahn.readnews.data.Article
import java.util.concurrent.Executor

class DataSourceFactory(
    private val api: NewsApiService,
    private val country : String,
    private val category: String,
    private val executor : Executor
) : DataSource.Factory<Int, Article>(){

    val sourceLiveData = MutableLiveData<PageKeyedArticleDataSource>()

    override fun create(): DataSource<Int, Article> {
        val source = PageKeyedArticleDataSource(
            country = country,
            category = category,
            api = api,
            retryExecutor = executor
        )
        sourceLiveData.postValue(source)

        return source
    }

}