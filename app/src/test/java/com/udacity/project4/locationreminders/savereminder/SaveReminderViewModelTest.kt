package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.TestData.testData
import com.udacity.project4.locationreminders.TestData.testData1
import com.udacity.project4.locationreminders.TestData.testData2
import com.udacity.project4.locationreminders.TestData.testPoi
import com.udacity.project4.locationreminders.TestData.toReminderData
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SaveReminderViewModelTest {

    // Subject under test
    private lateinit var reminderViewModel: SaveReminderViewModel

    // Use a fake data source to be injected into the viewmodel
    private lateinit var reminderDataSource: FakeDataSource


    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        reminderDataSource = FakeDataSource()
        reminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            reminderDataSource
        )
    }

    @After
    fun stopDown() {
        stopKoin()
    }

    @Test
    fun `save reminder and change loading state`()  {
        mainCoroutineRule.pauseDispatcher()
        reminderViewModel.saveReminder(testData.toReminderData())

        val value = reminderViewModel.showLoading.getOrAwaitValue()

        assertThat(value, `is`(true))
    }

    @Test
    fun `add new poi and check poi value`()  {
        mainCoroutineRule.pauseDispatcher()
        reminderViewModel.saveSelectedPoi(testPoi)

        val value = reminderViewModel.selectedPOI.value

        assertThat(value, `is`(testPoi))
    }

    @Test
    fun `validate reminder with empty title and change snackbar value`()  {
        mainCoroutineRule.pauseDispatcher()
        reminderViewModel.validateEnteredData(testData2.toReminderData())

        val value = reminderViewModel.showSnackBarInt.value

        assertThat(value, `is`(R.string.err_enter_title))
    }

    @Test
    fun `validate reminder with empty location and change snackbar value`()  {
        mainCoroutineRule.pauseDispatcher()
        reminderViewModel.validateEnteredData(testData1.toReminderData())

        val value = reminderViewModel.showSnackBarInt.value

        assertThat(value, `is`(R.string.err_select_location))
    }

    @Test
    fun `check if loading state is changing during reminders saving`() {
        mainCoroutineRule.pauseDispatcher()
        // Make the repository return errors.
        reminderViewModel.saveReminder(testData.toReminderData())

        assertThat(reminderViewModel.showLoading.value, `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(reminderViewModel.showLoading.value, `is`(false))
    }


    @Test
    fun `save and clear poi data`() {
        // Make the repository return errors.
        reminderViewModel.saveSelectedPoi(testPoi)

        assertNotNull(reminderViewModel.selectedPOI.value)
        assertNotNull(reminderViewModel.latitude.value)
        assertNotNull(reminderViewModel.longitude.value)
        assertNotNull(reminderViewModel.reminderSelectedLocationStr.value)

        reminderViewModel.onClear()

        assertNull(reminderViewModel.selectedPOI.value)
        assertNull(reminderViewModel.latitude.value)
        assertNull(reminderViewModel.longitude.value)
        assertNull(reminderViewModel.reminderSelectedLocationStr.value)
    }
}
