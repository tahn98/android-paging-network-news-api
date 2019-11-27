package com.tahn.readnews.repository

import com.tahn.readnews.data.Article
import com.tahn.readnews.data.Listing

interface NewsRepository {
    fun getTopHeadlines(country : String, category : String, pageSize : Int) : Listing<Article>
}