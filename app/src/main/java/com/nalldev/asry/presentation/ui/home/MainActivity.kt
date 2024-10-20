package com.nalldev.asry.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityMainBinding
import com.nalldev.asry.presentation.ui.auth.AuthActivity
import com.nalldev.asry.presentation.ui.story_maps.StoryMapsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initObserver()
        initView()
        initListener()
    }

    private fun initObserver() = with(viewModel) {

        navigateState.observe(this@MainActivity) { navigateState ->
            if (navigateState == MainViewModel.NavigateState.AUTH) {
                navigateToAuth()
            }
        }

        toastEvent.observe(this@MainActivity) { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {

    }

    private fun initListener() = with(binding) {

        actionMap.setOnClickListener {
            navigateToMap()
        }

        actionLogout.setOnClickListener {
            viewModel.doLogOut()
        }

    }

    private fun navigateToAuth() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToMap() {
        val intent = Intent(this@MainActivity, StoryMapsActivity::class.java)
        startActivity(intent)
    }
}