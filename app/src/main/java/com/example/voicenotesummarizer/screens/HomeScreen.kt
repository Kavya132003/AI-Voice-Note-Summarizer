package com.example.voicenotesummarizer.screens

import com.kavya.voicenotesummarizer.data.AIService
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeScreen() {
    val aiService = AIService()

    var resultText by remember { mutableStateOf("Your summarized text will appear here...") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val text = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = text?.get(0) ?: "No speech detected"

            resultText = "Processing..."

            aiService.summarizeText(spokenText) { summary ->
                resultText = summary
            }
        }
    }

    // 💜 Lavender Gradient Background
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB993D6),
            Color(0xFF8CA6DB)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "AI Voice Note Summarizer",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🎤 Record Button
            Button(
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    launcher.launch(intent)
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("🎤 Record Voice", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✍️ Text Button
            Button(
                onClick = {
                    resultText = "Text input feature coming soon..."
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("✍️ Enter Text", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ✨ Glass Card Effect
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = resultText,
                    color = Color.White,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}