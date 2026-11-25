package com.smartpet.feed.work

import android.content.Context
import androidx.work.*
import com.smartpet.feed.data.AppDatabase
import com.smartpet.feed.data.FeedingLog
import com.smartpet.feed.data.Schedule
import com.smartpet.feed.device.ServiceLocator
import java.util.Calendar
import java.util.concurrent.TimeUnit

class FeedWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val grams = inputData.getInt("grams", 30)
        val r = ServiceLocator.device.feed(grams)
        AppDatabase.get(applicationContext).dao().insertLog(
            FeedingLog(
                timestamp = System.currentTimeMillis(),
                grams = grams,
                success = r.isSuccess,
                source = "horario"
            )
        )
        return Result.success()
    }
}

fun scheduleFeed(ctx: Context, s: Schedule) {
    val now = Calendar.getInstance()
    val next = (now.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, s.hour)
        set(Calendar.MINUTE, s.minute)
        set(Calendar.SECOND, 0)
        if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
    }
    val req = PeriodicWorkRequestBuilder<FeedWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(next.timeInMillis - now.timeInMillis, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("grams" to s.grams))
        .build()
    WorkManager.getInstance(ctx)
        .enqueueUniquePeriodicWork("feed_${s.id}", ExistingPeriodicWorkPolicy.UPDATE, req)
}

fun cancelFeed(ctx: Context, id: Int) {
    WorkManager.getInstance(ctx).cancelUniqueWork("feed_$id")
}