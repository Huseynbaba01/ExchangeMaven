package com.codinghuseyn.exchangemaven.presentation.workmanager

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.codinghuseyn.exchangemaven.R
import com.codinghuseyn.exchangemaven.domain.repository.ExchangeRepository
import com.codinghuseyn.exchangemaven.presentation.activity.MainActivity
import com.codinghuseyn.exchangemaven.utils.Constants
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
    private val exchangeRepository: ExchangeRepository
) : Worker(applicationContext, workerParameters) {
    override fun doWork(): Result {
        doUpdate()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    fun doUpdate() {
        val from = inputData.getString("from")
        val to = inputData.getString("to")
        val min = inputData.getDouble("min", -1.0)
        val max = inputData.getDouble("max", -1.0)
        val quantity = inputData.getDouble("quantity", -1.0)

        val handler = CoroutineExceptionHandler { _, t ->
            println("Problem occurred! -- ${t.message}")
        }
        CoroutineScope(Dispatchers.Main+handler).launch {
            val rate = exchangeRepository.exchange(from!!, to!!)

            if (quantity * rate <= min || quantity * rate >= max) {
                val notificationIntent = Intent(applicationContext, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
                val builder =
                    NotificationCompat.Builder(applicationContext, Constants.DEFAULT_CHANNEL_ID)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(Color.YELLOW)
                        .setContentIntent(pendingIntent)
                        .setContentText("Current exchange rates are out of your limits!")
                        .setContentTitle("Min. limit!")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)


                NotificationManagerCompat.from(applicationContext).notify(5, builder.build())
            }
        }
    }
}