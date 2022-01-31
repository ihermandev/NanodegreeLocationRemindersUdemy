@file:Suppress("IllegalIdentifier")

package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private lateinit var database: RemindersDatabase

    private val testData = ReminderDTO(
        title = "Title",
        description = "Description",
        location = "Warsaw",
        latitude = 52.228155644591226,
        longitude = 21.0033821602075540
    )

    private val testData1 = ReminderDTO(
        title = "Title",
        description = "Description",
        location = "Poland",
        latitude = 53.228155644591226,
        longitude = 20.0033821602075540
    )

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun `save ReminderDTO to database and get by ID`() = runBlockingTest {
        database.reminderDao().saveReminder(testData)

        val loaded = database.reminderDao().getReminderById(testData.id)

        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(testData.id))
        assertThat(loaded.description, `is`(testData.description))
        assertThat(loaded.location, `is`(testData.location))
        assertThat(loaded.latitude, `is`(testData.latitude))
        assertThat(loaded.longitude, `is`(testData.longitude))
    }

    @Test
    fun `save ReminderDTO to database and delete it`() = runBlockingTest {
        database.reminderDao().saveReminder(testData)

        database.reminderDao().deleteReminder(testData)

        val loaded = database.reminderDao().getReminderById(testData.id)

        assertNull(loaded)
    }

    @Test
    fun `save few ReminderDTO to database and delete all`() = runBlockingTest {
        database.reminderDao().saveReminder(testData)
        database.reminderDao().saveReminder(testData1)

        database.reminderDao().deleteAllReminders()

        val loaded = database.reminderDao().getReminders()

        assertEquals(loaded.size, 0)
    }

    @Test
    fun `save few ReminderDTO to database`() = runBlockingTest {
        database.reminderDao().saveReminder(testData)
        database.reminderDao().saveReminder(testData1)

        val loaded = database.reminderDao().getReminders()

        assertEquals(loaded.size, 2)
    }

    @Test
    fun `get invalid data from database`() = runBlockingTest {
        database.reminderDao().saveReminder(testData)
        val invalidID = "123"
        val loaded = database.reminderDao().getReminderById(invalidID)

        assertNull(loaded)

    }

}
