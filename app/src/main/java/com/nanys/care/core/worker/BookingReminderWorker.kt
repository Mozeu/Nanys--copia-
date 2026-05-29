package com.nanys.care.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nanys.care.NanysApplication
import com.nanys.care.core.util.DateUtil
import java.util.concurrent.TimeUnit

class BookingReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val bookingId = inputData.getLong(KEY_BOOKING_ID, -1)
        val tutorEmail = inputData.getString(KEY_TUTOR_EMAIL).orEmpty()
        val caregiverEmail = inputData.getString(KEY_CAREGIVER_EMAIL).orEmpty()
        val app = applicationContext as NanysApplication
        val container = app.container
        container.notificationHelper.showNotification(
            "Recordatorio de cita",
            "Tu cita #$bookingId comienza en 15 minutos",
            bookingId.toInt()
        )
        container.simulationService.simulateEmail(tutorEmail, "Recordatorio de cita próxima")
        container.simulationService.simulateEmail(caregiverEmail, "Recordatorio de cita próxima")
        return Result.success()
    }

    companion object {
        const val KEY_BOOKING_ID = "booking_id"
        const val KEY_TUTOR_EMAIL = "tutor_email"
        const val KEY_CAREGIVER_EMAIL = "caregiver_email"

        fun schedule(context: Context, bookingId: Long, date: String, timeSlot: String, tutorEmail: String, caregiverEmail: String) {
            val delay = DateUtil.reminderDelayMillis(date, timeSlot) ?: return
            val request = OneTimeWorkRequestBuilder<BookingReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        KEY_BOOKING_ID to bookingId,
                        KEY_TUTOR_EMAIL to tutorEmail,
                        KEY_CAREGIVER_EMAIL to caregiverEmail
                    )
                )
                .addTag("booking_reminder_$bookingId")
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
