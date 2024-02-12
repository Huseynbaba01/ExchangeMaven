package com.codinghuseyn.exchangemaven.domain.repository

interface ExchangeRepository {
    suspend fun getListsQuotes(): List<String>

    suspend fun exchange(from: String, to: String): Double
}