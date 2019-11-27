package com.tahn.readnews.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.tahn.readnews.repository.NewsRepository

class NewsViewModel(private val repository: NewsRepository) : ViewModel(){

    private val category = MutableLiveData<String>()

    private val articleResult = map(category) {
        repository.getTopHeadlines("us", it , PAGE_SIZE)
    }

    val listArticles = switchMap(articleResult) { it.pagedList }
    val networkState = switchMap(articleResult) { it.networkState }
    val refreshState = switchMap(articleResult) { it.refreshState }

    fun refresh() {
        articleResult.value?.refresh?.invoke()
    }

    fun showResultsByCategory(category : String) : Boolean{
        if (this.category.value == category){
            return false
        }
        this.category.value = category
        return true
    }

    fun retry() {
        val listing = articleResult?.value
        listing?.retry?.invoke()
    }

    fun currentCategory(): String? = category.value

    companion object {
        const val PAGE_SIZE: Int = 6
    }
}