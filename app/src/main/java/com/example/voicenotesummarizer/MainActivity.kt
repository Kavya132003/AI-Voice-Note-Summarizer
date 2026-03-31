package com.kavya.voicenotesummarizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kavya.voicenotesummarizer.data.AIService

class MainActivity : ComponentActivity() {

    private val aiService = AIService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VoiceNoteApp()
        }
    }

    @Composable
    fun VoiceNoteApp() {

        var inputText by remember { mutableStateOf("") }
        var summaryText by remember { mutableStateOf("") }
        var keyPointsText by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }

        MaterialTheme {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // 🔤 Input Field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Enter or paste text") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🚀 Button
                Button(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            isLoading = true

                            aiService.summarizeText(inputText) { response ->

                                val parts = response.split("\n\n")

                                summaryText = parts.getOrNull(0) ?: ""
                                keyPointsText = parts.getOrNull(1) ?: response

                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Summary")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ⏳ Loading
                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 🧠 Summary Section
                if (summaryText.isNotEmpty()) {

                    Text(
                        text = "🧠 Summary",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = summaryText,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 📌 Key Points Section
                if (keyPointsText.isNotEmpty()) {

                    Text(
                        text = "📌 Key Points",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = keyPointsText,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}