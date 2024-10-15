package com.nalldev.asry.presentation.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivitySplashBinding
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
        initView()
    }

    private fun initObserver() {
        viewModel.navigationEvent.observe(this) { navigationEvent ->
            when (navigationEvent) {
                SplashViewModel.NavigationDestination.MAIN_ACTIVITY -> {
                    println("KE MAIN BERHASIL")
                }

                SplashViewModel.NavigationDestination.LOGIN_ACTIVITY -> {
                    println("KE LOGIN BERHASIL")
                }

                else -> {
                    println("KE LOGIN GAGAL")
                }
            }
        }
    }

    private fun initView() {
        binding.root.post {
            binding.root.progress = viewModel.motionProgress
            binding.root.transitionToEnd {
                viewModel.checkUserSession()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.motionProgress = binding.root.progress
    }
}