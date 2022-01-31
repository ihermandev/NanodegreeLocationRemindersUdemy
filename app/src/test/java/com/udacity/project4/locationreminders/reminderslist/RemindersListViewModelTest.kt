package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.TestData.testData
import com.udacity.project4.locationreminders.TestData.testData1
import com.udacity.project4.locationreminders.TestData.testData2
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RemindersListViewModelTest {

    // Subject under test
    private lateinit var reminderViewModel: RemindersListViewModel

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
        reminderViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            reminderDataSource
        )
    }

    @After
    fun stopDown() {
        stopKoin()
    }

    @Test
    fun `check initialization of reminder list`() {
        val dataList = mutableListOf<ReminderDTO>(
            testData,
            testData1,
            testData2)

        reminderDataSource = FakeDataSource(dataList)

        reminderViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            reminderDataSource
        )

        reminderViewModel.loadReminders()

        assertThat(reminderViewModel.remindersList.getOrAwaitValue().size,
            `is`(dataList.size))

    }

    @Test
    fun `change state of showNoData for empty list`()  {
        mainCoroutineRule.pauseDispatcher()

        reminderViewModel.loadReminders()
        reminderViewModel.invalidateShowNoData()

        val value = reminderViewModel.showNoData.value

        assertThat(value, `is`(true))
    }

    @Test
    fun `check if snackbar appears when an exception caught`() {
        // Make the repository return errors.
        reminderDataSource.setReturnError(true)
        reminderViewModel.loadReminders()

        assertThat(reminderViewModel.showSnackBar.value, `is`("Test exception"))
    }

    @Test
    fun `check if loading state is changing during reminders loading`() {
        mainCoroutineRule.pauseDispatcher()
        // Make the repository return errors.
        reminderViewModel.loadReminders()

        assertThat(reminderViewModel.showLoading.value, `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(reminderViewModel.showLoading.value, `is`(false))
    }

}
