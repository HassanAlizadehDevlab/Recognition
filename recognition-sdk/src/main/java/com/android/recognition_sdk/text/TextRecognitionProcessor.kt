package com.android.recognition_sdk.text

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LiveData


internal interface TextRecognitionProcessor {
    fun setupCamera(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
    )

    fun takePicture(): LiveData<TextRecognitionResultInternal>
}

internal sealed class TextRecognitionResultInternal {
    data class Success(val data: String) : TextRecognitionResultInternal()
    data class Error(val message: String?) : TextRecognitionResultInternal()
    object Loading : TextRecognitionResultInternal()
}