package com.codinghuseyn.exchangemaven.presentation.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.codinghuseyn.exchangemaven.domain.repository.ExchangeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    val exchangeRepository: ExchangeRepository
    ): Worker(applicationContext, workerParameters) {
    override fun doWork(): Result {
        val from = inputData.getString("from")
        val to = inputData.getString("to")
        val min = inputData.getDouble("min", -1.0)
        val max = inputData.getDouble("max", -1.0)
        val quantity = inputData.getDouble("quantity", -1.0)

        val handler = CoroutineExceptionHandler{ _, _->
            println("Problem occurred!")
        }
        CoroutineScope(Dispatchers.IO).launch{
            val rate = exchangeRepository.exchange(from!!, to!!)

            if(quantity*rate >= max){
                //TODO
                return@launch
            }
            if(quantity*rate <= min){
                //TODO
                return@launch
            }
        }
        return Result.success()
    }

    fun doUpdate(){

    }
}