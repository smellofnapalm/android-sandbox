package com.bignerdranch.android.geoquiz

import CheatViewModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val EXTRA_COUNT_CHEATINGS = "com.bignerdranch.android.geoquiz.count_cheatings"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private val cheatViewModel : CheatViewModel by viewModels()

    private var answerIsTrue = false

    private var countCheatings = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        countCheatings = intent.getIntExtra(EXTRA_COUNT_CHEATINGS, 0)

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

    private fun updateCheatingStatus() {
        setAnswerShownResults(cheatViewModel.cheatButtonPressed)
        binding.answerTextView.setText(cheatViewModel.answerText)
        binding.cheatingTextView.text = when(countCheatings) {
            0 -> "You haven't cheated yet! And you shouldn't!"
            1 -> "You have already cheated once! Stop it!"
            2 -> "You have already cheated twice! Play fare!"
            else -> "You shouldn't see this message actually"
        }
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, countCheatings: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_COUNT_CHEATINGS, countCheatings)
            }
        }
    }
}