package com.udacity.project4.locationreminders

import androidx.lifecycle.Transformations.map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

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
        location = "",
        latitude = 53.228155644591226,
        longitude = 20.0033821602075540
    )

    val testData2 = ReminderDTO(
        title = "",
        description = "Description",
        location = "Poland",
        latitude = 53.228155644591226,
        longitude = 20.0033821602075540
    )

    val testPoi = PointOfInterest(
        LatLng(53.228155644591226, 20.0033821602075540),
        "1234",
        "Test Pin"
    )

    fun ReminderDTO.toReminderData(): ReminderDataItem {
        return ReminderDataItem(
            title = this.title,
            description = this.description,
            location = this.location,
            latitude = this.latitude,
            longitude = this.longitude,
            id = this.id
        )
    }

}
