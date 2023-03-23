package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val MAX_PERCENT = 100

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0

    private val userCorrectAnswer = mutableMapOf<String, String>()

    val getCurrentIndex: Int get() = currentIndex

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val lastQuestion: Boolean get() = currentIndex + 1 == questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun previousQuestion() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    fun setCorrectAnswer(key: String, value: String) {
        userCorrectAnswer[key] = value
    }

    fun resetCurrentIndex() {
        currentIndex = 0
    }

    fun userPercentageCorrect(): String {
        val percentage = (userCorrectAnswer.size * MAX_PERCENT) / currentIndex

        return "Congratulations! You got $percentage% of the questions correct"
    }
}
