package com.codinghuseyn.exchangemaven.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.codinghuseyn.exchangemaven.data.remote.api.WebServiceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(private val api: WebServiceApi): ViewModel() {
    fun getListsQuotes() = liveData {
        val response = api.getListQuotes()
        if(response.isSuccessful) emit(response.body()!!)
    }
}