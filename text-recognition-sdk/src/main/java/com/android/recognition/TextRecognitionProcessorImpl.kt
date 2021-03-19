package com.android.recognition

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.text_recognition_sdk.R
import com.android.utils.ImageUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import java.io.File
import java.io.IOException


// Document
// Unit test
internal class TextRecognitionProcessorImpl constructor(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) : TextRecognitionProcessor {

    private val cameraSelector: CameraSelector =
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

    private val preview = Preview.Builder().build()
    private val imageCapture = ImageCapture.Builder().build()
    private var cameraProvider: ProcessCameraProvider? = null
    private val textRecognitionObservable = MutableLiveData<TextRecognitionResultInternal>()
    private val onImageSavedCallback = object : ImageCapture.OnImageSavedCallback {
        override fun onError(exc: ImageCaptureException) {

        }

        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
            try {
                with(InputImage.fromFilePath(context, output.savedUri!!)) {
                    detectTexts(this)
                }
            } catch (e: IOException) {
                e.printStackTrace()

                textRecognitionObservable.value = TextRecognitionResultInternal.Error(e.message)
            }
        }

    }


    override fun setupCamera(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
    ) {
        this.cameraProvider = cameraProvider

        // The use case is bound to an Android Lifecycle with the following code
        this.cameraProvider?.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        // PreviewView creates a surface provider and is the recommended provider
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    override fun takePicture(): LiveData<TextRecognitionResultInternal> {
        val photoFile = File(
            ImageUtils.getGalleryPath(),
            ImageUtils.getNewFileName()
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            onImageSavedCallback
        )

        textRecognitionObservable.value = TextRecognitionResultInternal.Loading
        return textRecognitionObservable
    }

    private fun detectTexts(image: InputImage) {
        TextRecognition.getClient().process(image)
            .addOnSuccessListener { firebaseVisionText ->
                Log.d(TAG, "Text Recognition succeed")

                if (firebaseVisionText.text.isEmpty())
                    textRecognitionObservable.value =
                        TextRecognitionResultInternal.Error(context.getString(R.string.could_not_find_anything))
                else
                    textRecognitionObservable.value =
                        TextRecognitionResultInternal.Success(firebaseVisionText.text)

            }.addOnFailureListener { e ->
                Log.d(TAG, "Text Recognition failed")

                textRecognitionObservable.value =
                    TextRecognitionResultInternal.Error(e.message)
            }
    }

    companion object {
        private val TAG = TextRecognitionProcessorImpl::class.simpleName
    }

}