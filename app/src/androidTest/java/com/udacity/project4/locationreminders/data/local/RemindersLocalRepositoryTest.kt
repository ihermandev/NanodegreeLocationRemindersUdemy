@file:Suppress("IllegalIdentifier")

package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.TestData.testData
import com.udacity.project4.TestData.testData1
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.dto.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var localDataSource: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            RemindersLocalRepository(
                database.reminderDao(),
                Dispatchers.Main
            )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun `save reminder and retrieve reminder`() = runBlocking {
        localDataSource.saveReminder(testData)

        val result = localDataSource.getReminder(testData.id)

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`("Title"))
        assertThat(result.data.description, `is`("Description"))
        assertThat(result.data.location, `is`("Warsaw"))
        assertThat(result.data.latitude, `is`(52.228155644591226))
        assertThat(result.data.longitude, `is`(21.0033821602075540))
    }

    @Test
    fun `retrieve reminder not Success result when wrong id is passed`() = runBlocking {
        val wrongId = "123"
        val result = localDataSource.getReminder(wrongId)
        assertThat(result.succeeded, `is`(false))
    }

    @Test
    fun `save reminders and delete all`() = runBlocking {
        localDataSource.saveReminder(testData)
        localDataSource.saveReminder(testData1)

        localDataSource.deleteAllReminders()

        val result = localDataSource.getReminders()

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.size, `is`(0))
    }
}
