package com.android.recognition_sdk.text

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.CameraXViewModel
import com.android.recognition_sdk.databinding.ActivityTextRecognitionBinding

/**
 * Internal Text Recognition activity that is accessible just
 * in this SDK to show camera, take picture and detect texts from it.
 * */
internal class TextRecognitionActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: ActivityTextRecognitionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        setupClickListeners()
    }

    private fun init() {
        setupViewModel()
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
                // set call TextRecognition class to setup camera
            }
    }

    private fun setupClickListeners() {
        binding.buttonTakePicture.setOnClickListener {
            // Take photo
        }
    }


}

