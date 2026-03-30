package com.example.voicenotesummarizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.voicenotesummarizer.screens.HomeScreen
import com.example.voicenotesummarizer.ui.theme.VoiceNoteSummarizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VoiceNoteSummarizerTheme {
                HomeScreen()
            }
        }
    }
}