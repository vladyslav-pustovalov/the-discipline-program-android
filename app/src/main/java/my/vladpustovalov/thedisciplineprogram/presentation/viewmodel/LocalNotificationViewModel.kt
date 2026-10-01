package my.vladpustovalov.thedisciplineprogram.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocalNotificationViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application)
