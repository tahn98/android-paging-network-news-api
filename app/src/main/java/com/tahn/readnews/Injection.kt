package com.tahn.readnews

import androidx.lifecycle.ViewModelProvider
import com.tahn.readnews.api.NewsApi
import com.tahn.readnews.api.NewsApiService
import com.tahn.readnews.repository.NewsRepository
import com.tahn.readnews.repository.NewsRepositoryImpl
import com.tahn.readnews.viewmodel.ViewModelFactory
import java.util.concurrent.Executors

object Injection {

    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private fun provideNewsRepository(): NewsRepository {
        return NewsRepositoryImpl(provideNewsApiService(), NETWORK_IO)
    }

    private fun provideNewsApiService() : NewsApiService{
        return NewsApiService(NewsApi.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideNewsRepository())
    }
}