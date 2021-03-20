package com.android.recognition.text_recognition

import android.app.Application
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ExecutionException


/** View model for interacting with CameraX.  */
internal class CameraXViewModel(application: Application) : AndroidViewModel(application) {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    fun getProcessCameraProvider(): LiveData<ProcessCameraProvider> {

        if (cameraProviderLiveData == null) {
            cameraProviderLiveData = MutableLiveData()
            ProcessCameraProvider.getInstance(getApplication()).apply {
                addListener(
                    {
                        try { cameraProviderLiveData!!.setValue(get()) }
                        catch (e: ExecutionException) {}
                        catch (e: InterruptedException) { }
                    },
                    ContextCompat.getMainExecutor(getApplication())
                )
            }
        }

        return cameraProviderLiveData!!
    }
}
