package com.kavya.voicenotesummarizer.data

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AIService {

    private val apiKey = "YOUR_API_KEY"

    fun summarizeText(input: String, callback: (String) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {

            val prompt = "Summarize this text into 3 bullet points:\n$input"

            try {
                // 🔥 Try modern model first
                val model = GenerativeModel(
                    modelName = "models/gemini-1.5-flash",
                    apiKey = apiKey
                )

                val response = model.generateContent(prompt)
                callback(response.text ?: "No response")

            } catch (e: Exception) {

                try {
                    // 🔁 Fallback to older stable model
                    val fallbackModel = GenerativeModel(
                        modelName = "models/text-bison-001",
                        apiKey = apiKey
                    )

                    val response = fallbackModel.generateContent(prompt)
                    callback(response.text ?: "Fallback worked but empty")

                } catch (e2: Exception) {
                    callback("❌ Both models failed.\n\nError: ${e2.message}")
                }
            }
        }
    }
}