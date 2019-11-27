package com.tahn.readnews.api

import com.tahn.readnews.data.ArticlesResponse
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category : String,
        @Query("page") page : Int,
        @Query("pageSize") pageSize : Int
    ) : Call<ArticlesResponse>


    companion object{
        private const val BASE_URL = "https://newsapi.org/v2/"
        fun create(httpUrl: String = BASE_URL): NewsApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(object : Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val origin = chain.request()
                        val request = origin.newBuilder()
                            .header("X-Api-Key", "10ae53f49102457fbe11813df9cab485")
                            .build()
                        return chain.proceed(request)
                    }
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}