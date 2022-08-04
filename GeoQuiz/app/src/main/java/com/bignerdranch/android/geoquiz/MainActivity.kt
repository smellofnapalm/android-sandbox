package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private val isAnswered = MutableList(questionBank.size) {false}
    private val isCorrectAnswered = MutableList(questionBank.size) {false}

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener{ checkAnswer(true) }
        binding.falseButton.setOnClickListener{ checkAnswer(false) }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateButtons()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener() {
            currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
            updateButtons()
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener { binding.nextButton.callOnClick() }

        updateQuestion()
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

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        }
        else {
            R.string.incorrect_toast
        }
        isCorrectAnswered[currentIndex] = (userAnswer == correctAnswer)
        isAnswered[currentIndex] = true
        checkForAllAnswers()
        updateButtons()
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !isAnswered[currentIndex]
        binding.falseButton.isEnabled = !isAnswered[currentIndex]
    }

    private fun checkForAllAnswers() {
        if (isAnswered.all { it }) {
            val countAnswers = isCorrectAnswered.count{ it }.toDouble()
            val ratioOfCorrectAnswers = countAnswers / questionBank.size
            Toast.makeText(this, "You have ${(ratioOfCorrectAnswers * 100).format(2)}% of correct answers!", Toast.LENGTH_LONG).show()
        }
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}