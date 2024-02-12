package com.codinghuseyn.exchangemaven.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.codinghuseyn.exchangemaven.domain.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(private val exchangeRepository: ExchangeRepository): ViewModel() {
    private val _quotesLiveData = MutableLiveData<List<String>>()
    val quotesLiveData: LiveData<List<String>>
        get() = _quotesLiveData

    val problemLiveData by lazy { MutableLiveData<String?>() }
    fun getListsQuotes() {
        viewModelScope.launch {
            try {
                _quotesLiveData.postValue(exchangeRepository.getListsQuotes())
                //TODO(Update data in the Local DB)
            }catch (e: Exception){
                problemLiveData.postValue(e.message)
            }
        }
    }

    fun exchange(from: String, to: String) = liveData{
        try {
            emit(exchangeRepository.exchange(from, to))
            //No need to add all pairs to the database, it will be nonsense
        }catch (e: Exception){
            problemLiveData.postValue(e.message)
        }
    }
}