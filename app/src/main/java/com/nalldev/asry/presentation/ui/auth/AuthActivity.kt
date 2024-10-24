package com.nalldev.asry.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityAuthBinding
import com.nalldev.asry.presentation.ui.home.MainActivity
import com.nalldev.asry.util.CommonHelper
import com.nalldev.asry.util.UIState
import com.nalldev.asry.util.clearAllFocus
import com.nalldev.asry.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<AuthViewModel>()

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

        binding.loadingView.isVisible = false
    }

    private fun initObserver() = with(viewModel) {
        motionProgress.observe(this@AuthActivity) { progress ->
            binding.motionLayout.progress = progress
        }

        transitionState.observe(this@AuthActivity) { motionState ->
            if (motionState == AuthViewModel.TransitionState.START) {
                binding.motionLayout.post {
                    binding.motionLayout.transitionToStart()
                }
            }
            if (motionState == AuthViewModel.TransitionState.END) {
                binding.motionLayout.post {
                    binding.motionLayout.transitionToEnd()
                }
            }
        }

        isRegisterFormValid.observe(this@AuthActivity) { isRegisterFormValid ->
            isFormRegisterValid(isRegisterFormValid)
        }

        isLoginFormValid.observe(this@AuthActivity) { isLoginFormValid ->
            isFormLoginValid(isLoginFormValid)
        }

        registerResult.observe(this@AuthActivity) { uiState ->
            when(uiState) {
                is UIState.Error -> binding.loadingView.isVisible = false
                is UIState.Loading -> binding.loadingView.isVisible = true
                is UIState.Success -> binding.loadingView.isVisible = false
            }
        }

        loginResult.observe(this@AuthActivity) { uiState ->
            when(uiState) {
                is UIState.Error -> binding.loadingView.isVisible = false
                is UIState.Loading -> binding.loadingView.isVisible = true
                is UIState.Success -> binding.loadingView.isVisible = false
            }
        }

        navigateState.observe(this@AuthActivity) { navigateState ->
            if (navigateState == AuthViewModel.NavigateState.LOGIN) {
                binding.motionLayout.post {
                    binding.motionLayout.transitionToStart()
                }
                clearInput()
            }
            if (navigateState == AuthViewModel.NavigateState.MAIN) {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        toastEvent.observe(this@AuthActivity) { message ->
            Toast.makeText(this@AuthActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListener() = with(binding) {
        onBackPressedDispatcher.addCallback {
            if (binding.motionLayout.progress > 0.0f) {
                binding.tvLogin.performClick()
            } else {
                finish()
            }
        }

        tvLogin.setOnClickListener {
            viewModel.setTransitionState(AuthViewModel.TransitionState.START)
            clearInput()
        }

        tvRegister.setOnClickListener {
            viewModel.setTransitionState(AuthViewModel.TransitionState.END)
            clearInput()
        }

        binding.edRegisterName.doOnTextChanged { text, _, _, _ ->
            viewModel.setRegisterName(text.toString())
        }
        binding.edRegisterEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setRegisterEmail(text.toString())
        }
        binding.edRegisterPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setRegisterPassword(text.toString())
        }

        binding.edLoginEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setLoginEmail(text.toString())
        }
        binding.edLoginPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setLoginPassword(text.toString())
        }

        binding.btnRegister.setOnClickListener {
            viewModel.doUserRegister()
        }
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            clearAllFocus()
            viewModel.doUserLogin()
        }
    }

    private fun isFormRegisterValid(isValid : Boolean) {
        binding.btnRegister.isEnabled = isValid
        binding.btnRegister.backgroundTintList = if (isValid) {
            ContextCompat.getColorStateList(this@AuthActivity, R.color.color_primary)
        } else {
            ContextCompat.getColorStateList(this@AuthActivity, R.color.button_disable)
        }
    }

    private fun isFormLoginValid(isValid : Boolean) {
        binding.btnLogin.isEnabled = isValid
        binding.btnLogin.backgroundTintList = if (isValid) {
            ContextCompat.getColorStateList(this@AuthActivity, R.color.color_primary)
        } else {
            ContextCompat.getColorStateList(this@AuthActivity, R.color.button_disable)
        }
    }

    private fun clearInput() {
        CommonHelper.clearEditTexts(listOf(
            binding.edRegisterName,
            binding.edRegisterEmail,
            binding.edRegisterPassword,
            binding.edLoginEmail,
            binding.edLoginPassword
        ))
        hideKeyboard()
        clearAllFocus()
    }

    override fun onPause() {
        super.onPause()
        viewModel.putMotionProgress(binding.motionLayout.progress)
    }
}