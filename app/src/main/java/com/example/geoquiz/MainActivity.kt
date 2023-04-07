package com.example.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

const val RADIUS_X_AND_Y = 10.0f

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
                    ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateQuestion()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

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
                        quizViewModel.clearData()
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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            RADIUS_X_AND_Y,
            RADIUS_X_AND_Y,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

    private fun updateQuestion() {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)

        quizViewModel.isCheater = quizViewModel.getAnswerCheater()

        if (quizViewModel.threeTimesCheat) binding.cheatButton.isEnabled = false

    }

    private fun checkAnswer(useAnswer: Boolean) {
        val contextView = findViewById<View>(android.R.id.content)

        Snackbar.make(contextView, quizViewModel.checkAnswer(useAnswer), Snackbar.LENGTH_SHORT)
            .show()
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
