package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

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

    private val isAnswered = MutableList(questionBank.size) {false}
    private val isCorrectAnswered = MutableList(questionBank.size) {false}

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveBack() {
        currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
    }
    val allAnswered: Boolean
        get() = isAnswered.all { it }
    val countCorrectAnswers: Int
        get() = isCorrectAnswered.count { it }
    val numberOfQuestions: Int
        get() = questionBank.size
    var isAnsweredThis: Boolean
        get() = isAnswered[currentIndex]
        set(value) { isAnswered[currentIndex] = value }
    var isCorrectAnsweredThis: Boolean
        get() = isCorrectAnswered[currentIndex]
        set(value) { isCorrectAnswered[currentIndex] = value }

}