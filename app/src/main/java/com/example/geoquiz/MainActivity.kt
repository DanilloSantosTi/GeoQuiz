package com.example.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val userCorrectAnswer = mutableMapOf<String, String>()

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        updateQuestion()

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            disableButtons()
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            disableButtons()
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            enableButtons()
        }

        binding.previousButton.setOnClickListener {
            if (currentIndex != 0) {
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
                disableButtons()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(useAnswer: Boolean) {
        val responseAnswer = questionBank[currentIndex].answer
        val contextView = findViewById<View>(android.R.id.content)

        val messageId =
            if (responseAnswer == useAnswer) {
                userCorrectAnswer["Question: $currentIndex"] = "Response: $useAnswer"
                R.string.correct_response
            } else R.string.incorrect_response

        Snackbar.make(contextView, messageId, Snackbar.LENGTH_SHORT).show()

        if (currentIndex + 1 == questionBank.size) {
            Snackbar.make(contextView, userPercentageCorrect(), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.Exit_button) {
                    currentIndex = 0
                    updateQuestion()
                    enableButtons()
                }
                .show()
        }
    }

    private fun disableButtons() {
        binding.falseButton.isEnabled = false
        binding.trueButton.isEnabled = false
    }

    private fun enableButtons() {
        binding.falseButton.isEnabled = true
        binding.trueButton.isEnabled = true
    }

    private fun userPercentageCorrect(): String {
        val percentage = (userCorrectAnswer.size * 100) / currentIndex

        return "Congratulations! You got $percentage% of the questions correct"
    }
}
