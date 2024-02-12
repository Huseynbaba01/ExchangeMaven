package com.codinghuseyn.exchangemaven.data.repository

import com.codinghuseyn.exchangemaven.data.remote.api.WebServiceApi
import com.codinghuseyn.exchangemaven.domain.repository.ExchangeRepository
import java.io.IOException
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(private val api: WebServiceApi): ExchangeRepository {
    override suspend fun getListsQuotes():List<String>{
        val response = api.getListQuotes()
        if (response.isSuccessful) return response.body()!!
        throw IOException("Error fetching quotes: ${response.code()}")
    }

    override suspend fun exchange(from: String, to: String): Double {
        val response = api.exchange(from, to)
        if(response.isSuccessful) return response.body()!!
        throw IOException("Error fetching quotes: ${response.code()}")
    }
}