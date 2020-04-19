package com.uj.bachelor_jlk700.examsystem.screens.Test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uj.bachelor_jlk700.examsystem.data.Answer
import com.uj.bachelor_jlk700.examsystem.data.AnsweredTest
import com.uj.bachelor_jlk700.examsystem.data.Question
import com.uj.bachelor_jlk700.examsystem.data.Test

class TestViewModel : ViewModel() {

    val q1 = Question("kot?", "cat", "dog",
        "horse", "watermelon", "ko")
    val q2 = Question("pies?", "a", "dog",
        "b", "s", "xd")
    val q3 = Question("fruit?", "cat", "dog",
        "horse", "watermelon", "ko")

    val test = Test(1, listOf(q1, q2, q3))
    var currentQuestionIndex = MutableLiveData(0)
    val question = MutableLiveData(test.questions[currentQuestionIndex.value!!])
    var info : MutableLiveData<String> = MutableLiveData("")
    val answers : MutableLiveData<MutableMap<Int, Answer>> = MutableLiveData(HashMap())
    var answer =  MutableLiveData(Answer(false, false, false, false, false))


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
}
