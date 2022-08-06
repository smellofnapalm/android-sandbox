package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener{ checkAnswer(true) }
        binding.falseButton.setOnClickListener{ checkAnswer(false) }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateButtons()
            updateQuestion()
            updateQuestionNumber()
        }

        binding.prevButton.setOnClickListener() {
            quizViewModel.moveBack()
            updateButtons()
            updateQuestion()
            updateQuestionNumber()
        }

        binding.questionTextView.setOnClickListener { binding.nextButton.callOnClick() }
    }

    override fun onResume() {
        super.onResume()
        updateButtons()
        updateScore()
        updateQuestion()
        updateQuestionNumber()
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
        updateScore()
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !quizViewModel.isAnsweredThis
        binding.falseButton.isEnabled = !quizViewModel.isAnsweredThis
    }

    private fun updateScore() {
        binding.scoreTextView.text = "${quizViewModel.countCorrectAnswers} / ${quizViewModel.numberOfQuestions}"
    }

    private fun updateQuestionNumber() {
        binding.indexTextView.text = (quizViewModel.currentIndex + 1).toString()
    }

    private fun checkForAllAnswers() {

        fun Double.format(digits: Int) = "%.${digits}f".format(this)

        if (quizViewModel.allAnswered) {
            val countAnswers = quizViewModel.countCorrectAnswers.toDouble()
            val ratioOfCorrectAnswers = countAnswers / quizViewModel.numberOfQuestions
            Toast.makeText(this, "You have ${(ratioOfCorrectAnswers * 100).format(2)}% of correct answers!", Toast.LENGTH_LONG).show()
        }
    }
}