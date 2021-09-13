package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToInt

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    private var questionBank = listOf(
        Question(R.string.question_australia, answer = true, result = false),
        Question(R.string.question_oceans, answer = true, result = false),
        Question(R.string.question_mideast, answer = false, result = false),
        Question(R.string.question_africa, answer = false, result = false),
        Question(R.string.question_americas, answer = true, result = false),
        Question(R.string.question_asia, answer = true, result = false),
    )

    private var currentIndex = 0
    private var rightAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
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
            if (currentIndex - 1 == -1) {
                currentIndex = questionBank.size
            }
            currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
            falseButton.isEnabled = true
            trueButton.isEnabled = true

        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }

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
        questionTextView.setText(questionTextResId)

    }

    private fun checkAnswer(userAnswer: Boolean) {
        var correctAnswer = questionBank[currentIndex].answer
        var finalResult: Double
        var totalQuestions = questionBank.size.toDouble()
        val messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            questionBank[currentIndex].result = true
        } else {
            messageResId = R.string.incorrect_toast
            questionBank[currentIndex].result = false
        }

        Log.v(TAG, currentIndex.toString() + " " + questionBank[currentIndex].result.toString())

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (currentIndex == 5) {
            rightAnswers = 0
            for (question in questionBank) {
                if (question.result) {
                    rightAnswers += 1
                }
            }

            finalResult = (rightAnswers / totalQuestions) * 100.00
            var toast = Toast.makeText(this, "${finalResult.roundToInt()} %", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 200)
            toast.show()
        }
    }
}