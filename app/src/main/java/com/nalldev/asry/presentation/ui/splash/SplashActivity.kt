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
import com.nalldev.asry.util.CommonHelper
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

        CommonHelper.useLightTheme(this)

        initObserver()
        initListener()
    }

    private fun initObserver() = with(viewModel) {
        motionProgress.observe(this@SplashActivity) { progress ->
            binding.root.progress = progress
        }

        navigationEvent.observe(this@SplashActivity) { navigationEvent ->
            if (navigationEvent == SplashViewModel.NavigationDestination.AUTH_ACTIVITY) {
                navigateToAuthActivity()
            }
            if (navigationEvent == SplashViewModel.NavigationDestination.MAIN_ACTIVITY) {
                navigateToMainActivity()
            }
        }
    }

    private fun initListener() = with(binding) {
        root.post {
            root.transitionToEnd {
                viewModel.checkUserSession()
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

    override fun onPause() {
        super.onPause()
        viewModel.storeMotionProgress(binding.root.progress)
    }
}