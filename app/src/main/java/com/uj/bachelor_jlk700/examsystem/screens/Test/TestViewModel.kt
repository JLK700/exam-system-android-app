package com.uj.bachelor_jlk700.examsystem.screens.Test

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.uj.bachelor_jlk700.examsystem.data.*

class TestViewModel : ViewModel() {

    val q1 = Question("kot?", "cat", "dog",
        "horse", "watermelon", "ko")
    val q2 = Question("pies?", "a", "dog",
        "b", "s", "xd")
    val q3 = Question("fruit?", "cat", "dog",
        "horse", "watermelon", "ko")
    val q4 = Question("fruit?", "cat", "dog",
        "horse", "watermelon", "ko")
    val q5 = Question("fruit?", "cat", "dog",
        "horse", "watermelon", "ko")

    val test = Test(1, 1, listOf(q1, q2, q3, q4, q5))
    var currentQuestionIndex = MutableLiveData(0)
    val question = MutableLiveData(test.questions[currentQuestionIndex.value!!])
    var info : MutableLiveData<String> = MutableLiveData()
    val answers : MutableLiveData<MutableMap<Int, Answer>> = MutableLiveData(HashMap())
    var answer =  MutableLiveData(Answer(false, false, false, false, false))

    val timer : CountDownTimer
    val timeLeft = MutableLiveData<Long>()
    val testTime = test.timeInMinutes * 60 * 1000
    val displayTime = Transformations.map(timeLeft) { timeLeft ->
        DateUtils.formatElapsedTime(timeLeft)
    }
    val timeLasted = MutableLiveData(TimeState.SAFE)


    fun nextQuestion() {
        currentQuestionIndex.value = currentQuestionIndex.value?.plus(1)
        question.value = test.questions[currentQuestionIndex.value!!]
        loadAnswer()
    }

    fun prevQuestion() {
        currentQuestionIndex.value = currentQuestionIndex.value?.minus(1)
        question.value = test.questions[currentQuestionIndex.value!!]
        loadAnswer()
    }

    fun saveAnswer(a: Boolean, b: Boolean, c: Boolean, d: Boolean, e: Boolean) {
        answers.value?.set(currentQuestionIndex.value!!, Answer(a,b,c,d,e))
    }

    private fun loadAnswer () {
        answer.value = answers.value?.get(currentQuestionIndex.value!!) ?: Answer(false, false, false, false, false)
    }

    fun generateAnswerTest () : AnsweredTest {
        val list = mutableListOf<Answer>()
        answers.value!!.forEach {
            list.add(it.value)
        }
        return AnsweredTest(test.id, list)
    }

    init {
        timer = object : CountDownTimer(testTime, 1000L) {
            override fun onFinish() {
                timeLasted.value = TimeState.END
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000L)
                if (millisUntilFinished == 3000L) {
                    timeLasted.value = TimeState.WARNING
                } else {
                    timeLasted.value = TimeState.SAFE
                }
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

}
