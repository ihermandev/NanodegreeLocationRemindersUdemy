package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.TestData.testData
import com.udacity.project4.locationreminders.data.FakeAndroidTestDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var dataSource: FakeAndroidTestDataSource
    private lateinit var appContext: Application

    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        //Get our fake repository
        dataSource = FakeAndroidTestDataSource()
        viewModel = RemindersListViewModel(appContext, dataSource)
        val myModule = module {
            viewModel {
                viewModel
            }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
    }

    @Test
    fun errorMessageAppearanceInSnackbar() = runBlockingTest {
        val message = "Test Error"

        val scenario =
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            it.view?.let { v -> Navigation.setViewNavController(v, navController) }
        }

        viewModel.showSnackBar.postValue(message)

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
    }

    @Test
    fun saveReminderAndDisplayIt() = runBlockingTest {
        dataSource.saveReminder(testData)

        val scenario =
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            it.view?.let { v -> Navigation.setViewNavController(v, navController) }
        }

        onView(withText(testData.title)).check(matches(isDisplayed()))
        onView(withText(testData.description)).check(matches(
            isDisplayed()))
        onView(withText(testData.location)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun navigateToSaveReminderScreenWhenFabClicked() = runBlockingTest {

        val scenario =
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            it.view?.let { v -> Navigation.setViewNavController(v, navController) }
        }

        onView(withId(R.id.addReminderFAB)).perform(click())


        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }
}
