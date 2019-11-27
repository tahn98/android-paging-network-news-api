package com.tahn.readnews.api

import com.tahn.readnews.data.ArticlesResponse

class NewsApiService(private val apiService: NewsApi) {
    fun getArticlesSync(country : String, page : Int, perPage : Int,
                        category : String,
                        onPrepared : () -> Unit,
                        onSuccess : (ArticlesResponse?) -> Unit,
                        onError : (String) -> Unit){
        val request = apiService.getTopHeadlines(country, category, page, perPage)
        onPrepared()
        ApiRequestHelper.syncRequest(request, onSuccess, onError)
    }
}