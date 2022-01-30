package com.udacity.project4.locationreminders

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.authentication.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_reminders.*

/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)
        observeAuth()
    }

    private fun observeAuth() {
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG, "Authenticated")
                }
                AuthenticationViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Log.i(TAG, "Unauthenticated, redirection to Authentication Screen")
                    finish()
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                }
                else -> Log.e(
                    TAG, "New $authenticationState state that doesn't require any UI change"
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = RemindersActivity::class.java.simpleName
    }
}
