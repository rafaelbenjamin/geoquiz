package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    /*init {
        Log.d(TAG, "ViewModel instance created")
    }*/

    /*override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }*/

    var rightAnswers = 0
    var currentIndex = 0
    var isCheater = false

    private var questionBank = listOf(
        Question(R.string.question_australia, answer = true, result = false),
        Question(R.string.question_oceans, answer = true, result = false),
        Question(R.string.question_mideast, answer = false, result = false),
        Question(R.string.question_africa, answer = false, result = false),
        Question(R.string.question_americas, answer = true, result = false),
        Question(R.string.question_asia, answer = true, result = false),
    )

    val questionBankList: List<Question>
        get() = questionBank

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    var currentQuestionResult: Boolean
        get() = questionBank[currentIndex].result
        set(value) {
            questionBank[currentIndex].result = value
        }

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val totalQuestions: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        if (currentIndex - 1 == -1) {
            currentIndex = questionBank.size
        }
        currentIndex = (currentIndex - 1) % questionBank.size
    }

}