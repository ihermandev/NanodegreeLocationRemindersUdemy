package com.udacity.project4

import com.udacity.project4.locationreminders.data.dto.ReminderDTO

object TestData {

    val testData = ReminderDTO(
        title = "Title",
        description = "Description",
        location = "Warsaw",
        latitude = 52.228155644591226,
        longitude = 21.0033821602075540
    )

     val testData1 = ReminderDTO(
        title = "Title",
        description = "Description",
        location = "Poland",
        latitude = 53.228155644591226,
        longitude = 20.0033821602075540
    )
}
