package com.nalldev.asry.presentation.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.OrientationEventListener
import android.view.Surface
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityCameraBinding
import com.nalldev.asry.presentation.ui.add_story.AddStoryActivity
import com.nalldev.asry.util.createTempFile
import com.nalldev.asry.util.getPackageInfo

class CameraActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<CameraViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var orientationEventListener: OrientationEventListener? = null
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

        initContract()

        checkPermission()
        initObservers()
        initListener()
    }

    private fun initContract() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                showPermissionDeniedDialog()
            }
        }

        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    navigateToAddStory(uri)
                }
            }
    }

    private fun checkPermission() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }
    }

    private fun initObservers() = with(viewModel) {
        cameraSelector.observe(this@CameraActivity) {
            startCamera()
        }

        rotation.observe(this@CameraActivity) { rotation ->
            updateViewRotation(rotation)
        }
    }

    private fun updateViewRotation(rotation: Int) {
        when (rotation) {
            Surface.ROTATION_0 -> {
                binding.actionSwap.rotation = 0f
                binding.actionGallery.rotation = 0f
            }
            Surface.ROTATION_90 -> {
                binding.actionSwap.rotation = 90f
                binding.actionGallery.rotation = 90f
            }
            Surface.ROTATION_180 -> {
                binding.actionSwap.rotation = 180f
                binding.actionGallery.rotation = 180f
            }
            Surface.ROTATION_270 -> {
                binding.actionSwap.rotation = 2700f
                binding.actionGallery.rotation = 270f
            }
        }
    }

    private fun initListener() = with(binding) {
        orientationEventListener = object : OrientationEventListener(this@CameraActivity) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                viewModel.updateRotation(orientation)
            }
        }

        actionSwap.setOnClickListener {
            viewModel.swapCamera()
        }

        actionGallery.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        actionCapture.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    viewModel.cameraSelector.value ?: CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    viewModel.imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val metadata = ImageCapture.Metadata()
        metadata.isReversedHorizontal = viewModel.cameraSelector.value == CameraSelector.DEFAULT_FRONT_CAMERA
        val outputOptions = ImageCapture.OutputFileOptions.Builder(createTempFile()).setMetadata(metadata).build()
        viewModel.imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        navigateToAddStory(uri)
                    }
                }

                override fun onError(e: ImageCaptureException) {
                    e.printStackTrace()
                }
            }
        )
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionDeniedDialog() {
        if (::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
        alertDialog = MaterialAlertDialogBuilder(this).apply {
            setCancelable(false)
            setTitle(context.getString(R.string.permission_denied_title))
            setMessage(context.getString(R.string.permission_denied_description))
            setPositiveButton(context.getString(R.string.setting)) { dialog, _ ->
                dialog.dismiss()
                navigateToSetting()
            }
            setNegativeButton(context.getString(R.string.back)) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.create()

        alertDialog.show()
    }

    private fun navigateToAddStory(uri : Uri) {
        val intent = Intent(this, AddStoryActivity::class.java)
        intent.putExtra(AddStoryActivity.EXTRAS, uri.toString())
        startActivity(intent)
    }

    private fun navigateToSetting() {
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.fromParts("package", getPackageInfo().packageName, null))

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener?.enable()
    }

    override fun onRestart() {
        super.onRestart()
        checkPermission()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener?.disable()
    }
}