package com.codinghuseyn.exchangemaven.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceApi {
    @GET("/listquotes")
    suspend fun getListQuotes(): Response<List<String>>

    @GET("/exchange")
    suspend fun exchange(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("q") quantity: Double
    ): Response<Int>
}