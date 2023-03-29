package com.example.geoquiz

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateQuestion()

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            disableOrEnableButton(false)
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            disableOrEnableButton(false)
        }

        binding.nextButton.setOnClickListener {
            val contextView = findViewById<View>(android.R.id.content)

            if (quizViewModel.lastQuestion) {
                disableOrEnableButton(buttonsResponseAnswer = false, nextAndPrevButtons = false)
                Snackbar.make(
                    contextView,
                    quizViewModel.userPercentageCorrect(),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.Exit_button) {
                        quizViewModel.resetCurrentIndex()
                        updateQuestion()
                        disableOrEnableButton(
                            buttonsResponseAnswer = true,
                            nextAndPrevButtons = true
                        )
                    }
                    .show()
            } else {
                quizViewModel.moveToNext()
                updateQuestion()
                disableOrEnableButton(true)
            }
        }

        binding.previousButton.setOnClickListener {
            if (quizViewModel.getCurrentIndex != 0) {
                quizViewModel.previousQuestion()
                updateQuestion()
                disableOrEnableButton(false)
            }
        }
    }

    private fun updateQuestion() {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer(useAnswer: Boolean) {
        val contextView = findViewById<View>(android.R.id.content)

        Snackbar.make(contextView, quizViewModel.checkAnswer(useAnswer), Snackbar.LENGTH_SHORT).show()
    }

    private fun disableOrEnableButton(
        buttonsResponseAnswer: Boolean,
        nextAndPrevButtons: Boolean = true
    ) {
        with(binding) {
            falseButton.isEnabled = buttonsResponseAnswer
            trueButton.isEnabled = buttonsResponseAnswer
            nextButton.isEnabled = nextAndPrevButtons
            previousButton.isEnabled = nextAndPrevButtons
        }
    }
}
