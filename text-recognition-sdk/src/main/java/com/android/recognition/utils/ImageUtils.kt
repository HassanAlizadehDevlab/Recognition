package com.android.recognition.utils

import android.os.Environment

internal object ImageUtils {

    private const val IMAGE_NAME_PREFIX = "text_recognition_"
    private const val IMAGE_NAME_SUFFIX = ".jpg"


    fun getGalleryPath(): String {
        return Environment.getExternalStorageDirectory().toString() +
                "/" +
                Environment.DIRECTORY_PICTURES + "/".also { it }
    }

    fun getNewFileName(): String {
        return StringBuilder().apply {
            append(IMAGE_NAME_PREFIX)
            append(System.currentTimeMillis())
            append(IMAGE_NAME_SUFFIX)
        }.toString()
    }
}