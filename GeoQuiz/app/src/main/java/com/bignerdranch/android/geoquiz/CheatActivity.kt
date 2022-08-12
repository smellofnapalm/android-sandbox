package com.bignerdranch.android.geoquiz

import CheatViewModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private val cheatViewModel : CheatViewModel by viewModels()

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.showAnswerButton.setOnClickListener {
            cheatViewModel.answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            cheatViewModel.cheatButtonPressed = true
            updateCheatingStatus()
        }
    }

    private fun setAnswerShownResults(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onResume() {
        super.onResume()
        updateCheatingStatus()
    }

    fun updateCheatingStatus() {
        setAnswerShownResults(cheatViewModel.cheatButtonPressed)
        binding.answerTextView.setText(cheatViewModel.answerText)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean)
            = Intent(packageContext, CheatActivity::class.java)
            .apply { putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue) }
    }
}