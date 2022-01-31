package com.udacity.project4.locationreminders

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.launch

class ReminderDescriptionViewModel(val app: Application, val dataSource: ReminderDataSource) :
    BaseViewModel(app) {

    fun removeReminder(reminderData: ReminderDataItem) {
        viewModelScope.launch {
            dataSource.deleteReminder(ReminderDTO(
                reminderData.title,
                reminderData.description,
                reminderData.location,
                reminderData.latitude,
                reminderData.longitude,
                reminderData.id
            ))
        }
    }
}
