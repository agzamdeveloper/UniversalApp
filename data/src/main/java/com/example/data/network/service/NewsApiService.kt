package com.example.data.network.service

import com.example.data.network.model.news.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {

    @GET
    suspend fun loadAllNewsByCategory(
        @Url fullUrl: String = FULL_URL,
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_LANGUAGE) language: String = "en",
        @Query(QUERY_PARAM_CATEGORY) category: String
    ): NewsResponseDto

    companion object {
        private const val QUERY_PARAM_API_KEY = "apiKey"
        private const val QUERY_PARAM_LANGUAGE = "language"
        private const val QUERY_PARAM_CATEGORY = "category"

        private const val FULL_URL = "https://newsapi.org/v2/top-headlines"
        private const val API_KEY = "ca6887ab3ea044f1939f094999d588b6"
    }
}