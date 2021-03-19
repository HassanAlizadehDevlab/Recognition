package com.android.recognition

import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * TextRecognition is a class for encapsulating text recognition usages between SDK and Host app.
 * */
object TextRecognition {

    private const val TEXT_RECOGNITION_DATA = "data"


    /** Generate success intent for internal usage */
    internal fun getSuccessResultIntent(data: String): Intent {
        return Intent().apply {
            with(TextRecognitionResult.Success(data)) {
                putExtra(TEXT_RECOGNITION_DATA, this)
            }
        }
    }

    /** Generate failure intent for internal usage */
    internal fun getErrorResultIntent(error: String?): Intent {
        return Intent().apply {
            with(TextRecognitionResult.Error(error)) {
                putExtra(TEXT_RECOGNITION_DATA, this)
            }
        }
    }

}


/**
 * Model class for response of text recognition
 * */
sealed class TextRecognitionResult : Parcelable {
    @Parcelize
    data class Success(val data: String) : TextRecognitionResult()

    @Parcelize
    data class Error(val message: String?) : TextRecognitionResult()
}