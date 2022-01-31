package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    private var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error(message = "Test exception", statusCode = 111)
        }
        reminders?.let { return Result.Success((it)) }
        return Result.Error(
            message = "Reminder not found",
            statusCode = 123
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderDTO) {
        reminders?.remove(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(message = "Test exception", statusCode = 111)
        }
        reminders?.firstOrNull { it.id == id }?.let { return Result.Success(it) }
        return Result.Error(
            message = "Reminder not found",
            statusCode = 123
        )
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }


}
