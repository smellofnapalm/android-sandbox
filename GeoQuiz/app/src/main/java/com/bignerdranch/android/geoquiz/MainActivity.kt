package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener{ checkAnswer(true) }
        binding.falseButton.setOnClickListener{ checkAnswer(false) }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateButtons()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener() {
            quizViewModel.moveBack()
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
        updateButtons()
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
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        }
        else {
            R.string.incorrect_toast
        }
        quizViewModel.isCorrectAnsweredThis = (userAnswer == correctAnswer)
        quizViewModel.isAnsweredThis = true
        checkForAllAnswers()
        updateButtons()
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !quizViewModel.isAnsweredThis
        binding.falseButton.isEnabled = !quizViewModel.isAnsweredThis
    }

    private fun checkForAllAnswers() {
        if (quizViewModel.allAnswered) {
            val countAnswers = quizViewModel.countCorrectAnswers.toDouble()
            val ratioOfCorrectAnswers = countAnswers / quizViewModel.numberOfQuestions
            Toast.makeText(this, "You have ${(ratioOfCorrectAnswers * 100).format(2)}% of correct answers!", Toast.LENGTH_LONG).show()
        }
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}