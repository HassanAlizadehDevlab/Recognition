package com.android.recognition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.recognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TEXT_RECOGNITION_REQUEST = 2020
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        with(TextRecognition.getIntent(this)) {
            startActivityForResult(this, TEXT_RECOGNITION_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TEXT_RECOGNITION_REQUEST) {
            if (resultCode == RESULT_OK)
                TextRecognition.getResult(data)?.let { handleTextRecognitionResult(it) }
            else Log.d(TAG, "Text recognition result is failed")
        }

    }

    private fun handleTextRecognitionResult(result: TextRecognitionResult) {
        when (result) {
            is TextRecognitionResult.Success -> {
                binding.textViewResult.text = result.data
            }
            is TextRecognitionResult.Error -> {
                binding.textViewResult.text = result.message
            }
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
    }
}