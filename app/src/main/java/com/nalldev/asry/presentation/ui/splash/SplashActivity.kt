package com.nalldev.asry.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivitySplashBinding
import com.nalldev.asry.presentation.ui.auth.AuthActivity
import com.nalldev.asry.presentation.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.motionProgress.observe(this) { progress ->
            binding.root.progress = progress
        }

        viewModel.navigationEvent.observe(this) { navigationEvent ->
            when (navigationEvent) {
                SplashViewModel.NavigationDestination.MAIN_ACTIVITY -> {
                    navigateToMainActivity()
                }
                else -> {
                    navigateToAuthActivity()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initListener() {
        binding.root.post {
            binding.root.transitionToEnd {
                viewModel.checkUserSession()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.storeMotionProgress(binding.root.progress)
    }
}