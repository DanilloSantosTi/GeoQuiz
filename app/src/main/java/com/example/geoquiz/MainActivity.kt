package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        val contextView = findViewById<View>(android.R.id.content)

        trueButton.setOnClickListener {
            Snackbar.make(contextView, R.string.correct_response, Snackbar.LENGTH_SHORT).show()
        }

        falseButton.setOnClickListener {
            Toast.makeText(this, R.string.incorrect_response, Toast.LENGTH_SHORT).show()
        }
    }
}
