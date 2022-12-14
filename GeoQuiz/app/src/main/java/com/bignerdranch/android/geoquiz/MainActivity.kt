package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheaterOnThis = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }

    }

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

        binding.prevButton.setOnClickListener {
            quizViewModel.moveBack()
            updateButtons()
            updateQuestion()
            updateQuestionNumber()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, quizViewModel.countCheetings)
            cheatLauncher.launch(intent)
        }

        binding.questionTextView.setOnClickListener { binding.nextButton.callOnClick() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }
    }

    override fun onResume() {
        super.onResume()
        banCheatingIfLimits()
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
        val messageResId = when {
            quizViewModel.isCheaterOnThis -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        quizViewModel.isCorrectAnsweredThis = (userAnswer == correctAnswer)
        quizViewModel.isAnsweredThis = true
        checkForAllAnswers()
        updateButtons()
        updateScore()
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !quizViewModel.isAnsweredThis
        binding.falseButton.isEnabled = !quizViewModel.isAnsweredThis
        binding.cheatButton.isEnabled = !quizViewModel.isCheaterOnThis
    }

    private fun updateScore() {
        binding.scoreTextView.text = "${quizViewModel.countCorrectAnswers} / ${quizViewModel.numberOfQuestions}"
    }

    private fun updateQuestionNumber() {
        binding.indexTextView.text = (quizViewModel.currentIndex + 1).toString()
    }

    private fun banCheatingIfLimits() {
        if (quizViewModel.isCheatLimitBeaten()) {
            quizViewModel.banCheating()
            Toast.makeText(this, "You have already made ${quizViewModel.cheatLimit} cheatings! Now you have to play fare!", Toast.LENGTH_LONG).show()
        }
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