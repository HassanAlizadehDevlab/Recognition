package com.android.recognition

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Model class for response of text recognition
 * */
sealed class TextRecognitionResult : Parcelable {
    @Parcelize
    data class Success(val data: String) : TextRecognitionResult()

    @Parcelize
    data class Error(val message: String?) : TextRecognitionResult()
}