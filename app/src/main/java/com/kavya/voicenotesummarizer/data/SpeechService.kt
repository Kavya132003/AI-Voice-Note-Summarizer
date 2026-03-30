package com.kavya.voicenotesummarizer.data

import android.content.Intent
import android.speech.RecognizerIntent

class SpeechService {

    fun getSpeechIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak now..."
            )
        }
    }
}