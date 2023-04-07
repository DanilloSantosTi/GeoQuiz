package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val MAX_PERCENT = 100
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val MAX_CHEAT = 3

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)


    private val userCorrectAnswer = mutableListOf<UserResponseQuestions>()
    private val userAnswerCheat = mutableMapOf<Int, Boolean>()

    val getCurrentIndex: Int get() = currentIndex

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val lastQuestion: Boolean get() = getCurrentIndex + 1 == questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun previousQuestion() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    fun setCorrectAnswer(question: String, response: String) {
        userCorrectAnswer.add(
            UserResponseQuestions(
                question = question,
                userResponse = response,
            )
        )
    }

    fun resetCurrentIndex() {
        currentIndex = 0
    }

    fun clearData(){
        userCorrectAnswer.clear()
    }

    fun userPercentageCorrect(): String {
        val percentage = (userCorrectAnswer.size * MAX_PERCENT) / questionBank.size
        Log.d("size userCorrectAnswer", "${userCorrectAnswer.size}")
        Log.d("size questionBank", "${questionBank.size}")

        return "Congratulations! You got $percentage% of the questions correct"
    }

    fun getAnswerCheater(): Boolean {
        return userAnswerCheat[getCurrentIndex] == true
    }

    val threeTimesCheat: Boolean get() = userAnswerCheat.size >= MAX_CHEAT

    fun checkAnswer(useAnswer: Boolean): Int {
        val responseAnswer = currentQuestionAnswer

        if (responseAnswer == useAnswer) {
            setCorrectAnswer(
                "Question: $getCurrentIndex",
                "Response: $useAnswer",
            )
        }

        userAnswerCheat[getCurrentIndex] = isCheater

        return when {
            isCheater -> R.string.judgment_toast
            responseAnswer == useAnswer -> R.string.correct_response
            else -> R.string.incorrect_response
        }
    }
}
