package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            falseButton.isEnabled = false
            trueButton.isEnabled = false

        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
            falseButton.isEnabled = true
            trueButton.isEnabled = true

        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }

        cheatButton.setOnClickListener {
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        questionTextView.setText(questionTextResId)

    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var finalResult: Double
        var totalQuestions = quizViewModel.totalQuestions.toDouble()
        val messageResId: Int

        when {
            quizViewModel.isCheater -> {
                messageResId = R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                messageResId = R.string.correct_toast
                quizViewModel.currentQuestionResult = true
            }
            else -> {
                messageResId = R.string.incorrect_toast
                quizViewModel.currentQuestionResult = false
            }
        }

        Log.v(
            TAG,
            quizViewModel.currentIndex.toString() + "" + quizViewModel.currentQuestionResult.toString()
        )

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (quizViewModel.currentIndex == 5) {
            quizViewModel.rightAnswers = 0

            for (question in quizViewModel.questionBankList) {
                if (question.result) {
                    quizViewModel.rightAnswers += 1
                }
            }

            finalResult = (quizViewModel.rightAnswers / totalQuestions) * 100.00
            var toast = Toast.makeText(this, "${finalResult.roundToInt()} %", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 200)
            toast.show()
        }
    }
}