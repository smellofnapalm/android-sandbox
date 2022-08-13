package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_ANSWERED_KEY = "IS_ANSWERED_KEY"
const val IS_CORRECT_ANSWERED_KEY = "IS_CORRECT_ANSWERED_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) { savedStateHandle[CURRENT_INDEX_KEY] = value }

    private val isAnswered = MutableList(questionBank.size) {false}
        get() = savedStateHandle[IS_ANSWERED_KEY] ?: field
    private val isCorrectAnswered = MutableList(questionBank.size) {false}
        get() = savedStateHandle[IS_CORRECT_ANSWERED_KEY] ?: field
    private var isCheater = MutableList(questionBank.size) {false}
        get() = savedStateHandle[IS_CHEATER_KEY] ?: field
        set(value) {
            field = value
            savedStateHandle[IS_CHEATER_KEY] = field
        }

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
        set(value) {
            isAnswered[currentIndex] = value
            savedStateHandle[IS_ANSWERED_KEY] = isAnswered
        }
    var isCorrectAnsweredThis: Boolean
        get() = isCorrectAnswered[currentIndex]
        set(value) {
            isCorrectAnswered[currentIndex] = value
            savedStateHandle[IS_CORRECT_ANSWERED_KEY] = isCorrectAnswered
        }
    var isCheaterOnThis: Boolean
        get() = isCheater[currentIndex]
        set(value) {
            isCheater[currentIndex] = value
            savedStateHandle[IS_CHEATER_KEY] = isCheater
        }

    val cheatLimit = 3
    val countCheetings: Int
        get() = isCheater.count { it }

    fun isCheatLimitBeaten() = (countCheetings >= cheatLimit)
    fun banCheating() {
        isCheater = MutableList(questionBank.size) {true}
    }

}