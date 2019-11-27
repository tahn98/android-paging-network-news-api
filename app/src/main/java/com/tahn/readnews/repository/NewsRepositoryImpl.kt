package com.tahn.readnews.repository

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tahn.readnews.api.NewsApiService
import com.tahn.readnews.data.Article
import com.tahn.readnews.data.Listing
import java.util.concurrent.Executor

class NewsRepositoryImpl(
    private val api : NewsApiService,
    private val networkExecutor : Executor
) : NewsRepository {
    override fun getTopHeadlines(country: String, category: String, pageSize: Int): Listing<Article> {
        val factory = DataSourceFactory(
            api,
            country,
            category,
            networkExecutor
        )

        val config = pagedListConfig(pageSize)

        val livePagedList = LivePagedListBuilder(factory, config)
            .setFetchExecutor(networkExecutor)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = switchMap(factory.sourceLiveData) { it.networkState },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            refreshState = switchMap(factory.sourceLiveData) { it.initialLoad })
    }

    private fun pagedListConfig(pageSize: Int) : PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize)
            .setPageSize(pageSize)
            .build()
    }
}