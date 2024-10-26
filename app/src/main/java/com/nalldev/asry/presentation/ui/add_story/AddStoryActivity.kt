package com.nalldev.asry.presentation.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityAddStoryBinding
import com.nalldev.asry.util.addPersistenceUri
import com.nalldev.asry.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddStoryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AddStoryViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initIntentExtras()
        initContract()
        initObserver()
        initListener()
    }

    private fun initIntentExtras() {
        try {
            addPersistenceUri(intent.getStringExtra(EXTRAS).toString().toUri()) { uri ->
                viewModel.setCurrentUri(uri)
                binding.ivStoryPhoto.setImageURI(uri)
            }
        } catch (e : Exception) {
            intent.getStringExtra(EXTRAS)?.toUri()?.let { uri ->
                viewModel.setCurrentUri(uri)
                binding.ivStoryPhoto.setImageURI(uri)
            } ?: run {
                showToast(getString(R.string.convert_uri_fail_msg))
            }
        }
    }

    private fun initContract() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                viewModel.startLocationUpdates()
            } else {
                binding.cbLocation.isChecked = false
                showPermissionDeniedDialog()
            }
        }
    }

    private fun initObserver() = with(viewModel) {
        coordinate.observe(this@AddStoryActivity) { latLng ->
            if (latLng != null) {
                println("LOKASI SAYA DAPATKAN : ${latLng}")
            } else {
                stopLocationUpdates()
            }
        }
    }

    private fun initListener() = with(binding) {
        cbLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkPermission()
            } else {
                viewModel.setCoordinate(null)
            }

            tvLocationDescription.isVisible = isChecked
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkPermission() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            viewModel.startLocationUpdates()
        }
    }

    private fun showPermissionDeniedDialog() {
        if (::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
        alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_denied_title))
            .setMessage(getString(R.string.permission_denied_description))
            .setPositiveButton(getString(R.string.setting)) { dialog, _ ->
                dialog.dismiss()
                navigateToSettings()
            }.create()

        alertDialog.show()
    }

    private fun navigateToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdates()
    }

    companion object {
        const val EXTRAS = "extras"
    }
}