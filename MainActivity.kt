package com.example.testmcpapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPrompt: EditText
    private lateinit var buttonSend: Button
    private lateinit var textViewResponse: TextView
    private lateinit var generativeModel: GenerativeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPrompt = findViewById(R.id.editTextPrompt)
        buttonSend = findViewById(R.id.buttonSend)
        textViewResponse = findViewById(R.id.textViewResponse)

        generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = "AIzaSyAL-JzJ7kmh7MePAHg2r_0s2DCgftfe5iA"
        )

        buttonSend.setOnClickListener {
            sendPromptToGemini()
        }
    }

    private fun sendPromptToGemini() {
        val prompt = editTextPrompt.text.toString().trim()

        if (prompt.isEmpty()) {
            Toast.makeText(this, "Please enter a prompt", Toast.LENGTH_SHORT).show()
            return
        }

        // Disable button while processing
        buttonSend.isEnabled = false
        buttonSend.text = "Sending..."
        textViewResponse.text = "Processing your request..."


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generativeModel.generateContent(prompt)

                withContext(Dispatchers.Main) {
                    textViewResponse.text = response.text ?: "No response received"
                    buttonSend.isEnabled = true
                    buttonSend.text = "Send to Gemini"
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    textViewResponse.text = "Error: ${e.message}"
                    buttonSend.isEnabled = true
                    buttonSend.text = "Send to Gemini"
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
