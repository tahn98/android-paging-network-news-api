package com.tahn.readnews.data

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    val totalResults : Int,
    @SerializedName("articles")
    val data : List<Article>)