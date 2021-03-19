package com.android.recognition

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * TextRecognition is a class for encapsulating text recognition usages between SDK and Host app.
 * */
object TextRecognition {

    private const val TEXT_RECOGNITION_DATA = "data"


    /**
     * This method call from Host app to get an intent for opening [TextRecognitionActivity] page.
     *
     * @param context of Host app current page.
     * @return an intent to open [TextRecognitionActivity].
     * */
    fun getIntent(context: Activity): Intent {
        return Intent(context, TextRecognitionActivity::class.java)
    }


    /** A function to parse text recognition intent.
     *
     * @param intent is come from onActivityResult function to parse result.
     * @return [TextRecognitionResult] is a sealed class for result of Text Recognition process.
     * */
    fun getResult(intent: Intent?): TextRecognitionResult? {
        return intent?.extras?.get(TEXT_RECOGNITION_DATA) as? TextRecognitionResult
    }


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