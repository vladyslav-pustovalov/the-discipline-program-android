package my.vladpustovalov.thedisciplineprogram.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import my.vladpustovalov.thedisciplineprogram.domain.notification.LocalNotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocalNotificationViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val notificationManager = LocalNotificationManager(application)

    fun schedulePaymentReminder() {
        notificationManager.scheduleMonthlyPaymentReminder()
    }

    fun cancelPaymentReminder() {
        notificationManager.cancelMonthlyPaymentReminder()
    }
}
