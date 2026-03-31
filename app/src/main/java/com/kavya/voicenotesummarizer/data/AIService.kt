package com.kavya.voicenotesummarizer.data

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException

class AIService {

    private val client = OkHttpClient()

    private val apiKey = "YOUR_API_KEY"

    fun summarizeText(input: String, callback: (String) -> Unit) {

        val prompt = """
            Summarize the following text:

            1. Give a short summary (2 lines)
            2. Then give 3 bullet key points

            Text:
            $input
        """.trimIndent()

        val part = JSONObject()
        part.put("text", prompt)

        val partsArray = JSONArray()
        partsArray.put(part)

        val content = JSONObject()
        content.put("parts", partsArray)

        val contentsArray = JSONArray()
        contentsArray.put(content)

        val json = JSONObject()
        json.put("contents", contentsArray)

        val body = json.toString().toRequestBody(
            "application/json".toMediaTypeOrNull()
        )

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=$apiKey")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback("❌ Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    callback("❌ API Error: HTTP ${response.code}\n$responseBody")
                    return
                }

                try {
                    val jsonObject = JSONObject(responseBody)

                    val candidates = jsonObject.optJSONArray("candidates")

                    if (candidates != null && candidates.length() > 0) {

                        val parts = candidates
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")

                        val text = parts
                            .getJSONObject(0)
                            .getString("text")

                        callback(text)
                        return
                    }

                    callback("⚠️ No summary generated")

                } catch (e: Exception) {
                    callback("❌ Parsing error: ${e.message}")
                }
            }
        })
    }
}