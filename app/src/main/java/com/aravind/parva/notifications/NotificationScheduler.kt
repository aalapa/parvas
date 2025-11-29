package com.aravind.parva.notifications

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Schedules weekly notifications for accountability partner updates
 */
object NotificationScheduler {
    
    fun scheduleWeeklyReminder(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val weeklyWorkRequest = PeriodicWorkRequestBuilder<WeeklyReminderWorker>(
            7, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(7, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WeeklyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            weeklyWorkRequest
        )
    }

    fun cancelWeeklyReminder(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(WeeklyReminderWorker.WORK_NAME)
    }
}

