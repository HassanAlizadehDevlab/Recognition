# Text Recognition Android application
### In this repository we have Text Recognition SDK and implementation by MlKit.


## How to use it?
* Add classpath 'com.google.gms:google-services:4.3.5' to the build.gradle file in root of the main project.
* Then add this the Text Recognition SDK to your main project by "implementation project(':text-recognition-sdk')"
* Now you can use this SDK by <b>TextRecognitionSdk</b> class. It is the only class that you have access to it.
* Call <b>TextRecognitionSdk.getIntent(context)</b> to get the intent to TextRecognition Activity.


        with(TextRecognitionSdk.getIntent(this)) {
            startActivityForResult(this, TEXT_RECOGNITION_REQUEST)
        }

* It's important to override <b>onActivityResult</b> method and check response.

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == TEXT_RECOGNITION_REQUEST) {
                if (resultCode == RESULT_OK)
                    TextRecognitionSdk.getResult(data)?.let { handleTextRecognitionResult(it) }
                else Log.d(TAG, "Text recognition result is failed")
            }
        }

* To handle result, there is a kotlin sealed class that has Success and Error type. Most of the times for success and error result, 
the SDK returns RESULT_OK. So you need to handle result like this:


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
    
    
### For more information about implementation, clone the repository, see the app module and run the applcation ;)
