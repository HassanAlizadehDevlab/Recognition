package com.android.recognition.text_recognition

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.android.recognition.TextRecognitionSdk
import com.android.recognition.utils.PermissionUtils
import com.android.text_recognition_sdk.R
import com.android.text_recognition_sdk.databinding.ActivityTextRecognitionBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Internal Text Recognition activity that is accessible just
 * in this SDK to show camera, take picture and detect texts from it.
 * */
internal class TextRecognitionActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: ActivityTextRecognitionBinding
    private val textRecognition: TextRecognitionProcessor =
        TextRecognitionProcessorImpl(
            context = this,
            lifecycleOwner = this,
            preview = Preview.Builder().build(),
            imageCapture = ImageCapture.Builder().build(),
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        setupClickListeners()
    }

    private fun init() {
        if (PermissionUtils.allPermissionsGranted(this)) {
            setupViewModel()
        } else {
            PermissionUtils.getRuntimePermissions(this)
        }
    }

    private fun setupViewModel() {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CameraXViewModel::class.java]
            .getProcessCameraProvider()
            .observe(
                this
            ) { provider ->
                textRecognition.setupCamera(provider, binding.previewView)
            }
    }

    private fun setupClickListeners() {
        binding.buttonTakePicture.setOnClickListener {

            textRecognition.takePicture().also {
                observeTakingPictureResult(it)
            }
        }
    }

    private fun observeTakingPictureResult(liveData: LiveData<TextRecognitionResultInternal>) {
        liveData.observe(this) { response ->
            when (response) {
                is TextRecognitionResultInternal.Success -> {
                    Log.d(TAG, "Text Recognition succeed")
                    returnSuccess(response.data)
                }
                is TextRecognitionResultInternal.Error -> {
                    Log.d(TAG, "Text Recognition failed")

                    response.message?.let {
                        showMessage(it)
                    }
                }
                TextRecognitionResultInternal.Loading -> {
                    Log.d(TAG, "Text Recognition loading")

                    Snackbar.make(binding.root, getString(R.string.loading), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


    private fun returnSuccess(data: String) {
        TextRecognitionSdk.getSuccessResultIntent(data).also {
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    private fun returnFailed(error: String) {
        TextRecognitionSdk.getErrorResultIntent(error).also {
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    /************************ Permissions ************************/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (PermissionUtils.allPermissionsGranted(this)) {
            setupViewModel()
        } else {
            returnFailed(getString(R.string.permission_not_granted))
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private val TAG = TextRecognitionActivity::class.simpleName
    }

}

