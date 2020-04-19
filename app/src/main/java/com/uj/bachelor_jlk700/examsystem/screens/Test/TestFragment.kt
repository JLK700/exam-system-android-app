package com.uj.bachelor_jlk700.examsystem.screens.Test

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.uj.bachelor_jlk700.examsystem.R
import com.uj.bachelor_jlk700.examsystem.data.Answer
import com.uj.bachelor_jlk700.examsystem.data.Question
import com.uj.bachelor_jlk700.examsystem.data.UserInformation
import com.uj.bachelor_jlk700.examsystem.databinding.TestFragmentBinding
import timber.log.Timber

class TestFragment : Fragment() {

    companion object {
        fun newInstance() =
            TestFragment()
    }

    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: TestFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.test_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(TestViewModel::class.java)

        viewModel.info = MutableLiveData(TestFragmentArgs.fromBundle(arguments!!).userInfo)
        viewModel.question.observe(viewLifecycleOwner, Observer { q ->
            setQuestion(binding, q)
        })

        viewModel.answer.observe(viewLifecycleOwner, Observer { a ->
            loadAnswer(binding, a)
        })

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner, Observer { index ->
            if (index == viewModel.test.questions.size - 1) {
                binding.buttonTestFragmentNext.visibility = View.GONE
                binding.buttonTestFragmentEnd.visibility = View.VISIBLE
            } else {
                binding.buttonTestFragmentNext.visibility = View.VISIBLE
                binding.buttonTestFragmentEnd.visibility = View.GONE
            }
            if (index == 0) {
                binding.buttonTestFragmentPrevious.visibility = View.GONE
            } else {
                binding.buttonTestFragmentPrevious.visibility = View.VISIBLE
            }
        })

        binding.buttonTestFragmentNext.setOnClickListener {
            viewModel.saveAnswer(binding.checkBoxTestFragmentAnswerA.isChecked,
                                binding.checkBoxTestFragmentAnswerB.isChecked,
                                binding.checkBoxTestFragmentAnswerC.isChecked,
                                binding.checkBoxTestFragmentAnswerD.isChecked,
                                binding.checkBoxTestFragmentAnswerE.isChecked
            )
            clearTicks(binding)
            viewModel.nextQuestion()
        }

        binding.buttonTestFragmentPrevious.setOnClickListener {
            viewModel.saveAnswer(binding.checkBoxTestFragmentAnswerA.isChecked,
                binding.checkBoxTestFragmentAnswerB.isChecked,
                binding.checkBoxTestFragmentAnswerC.isChecked,
                binding.checkBoxTestFragmentAnswerD.isChecked,
                binding.checkBoxTestFragmentAnswerE.isChecked)
            clearTicks(binding)
            viewModel.prevQuestion()
        }

        binding.buttonTestFragmentEnd.setOnClickListener {
            // TODO : End Test
            viewModel.saveAnswer(binding.checkBoxTestFragmentAnswerA.isChecked,
                binding.checkBoxTestFragmentAnswerB.isChecked,
                binding.checkBoxTestFragmentAnswerC.isChecked,
                binding.checkBoxTestFragmentAnswerD.isChecked,
                binding.checkBoxTestFragmentAnswerE.isChecked)

            val action = TestFragmentDirections.actionTestFragmentToEndFragment()
            action.answeredTest = Gson().toJson(viewModel.generateAnswerTest())
            Navigation.findNavController(it).navigate(action)
        }

        return binding.root
    }

    private fun setQuestion (binding: TestFragmentBinding, q: Question) {
        binding.textViewTestFragmentAnswerA.text = q.answerA
        binding.textViewTestFragmentAnswerB.text = q.answerB
        binding.textViewTestFragmentAnswerC.text = q.answerC
        binding.textViewTestFragmentAnswerD.text = q.answerD
        binding.textViewTestFragmentAnswerE.text = q.answerE
        binding.textViewTestFragmentContent.text = q.content
    }

    private fun loadAnswer (binding: TestFragmentBinding, answer: Answer) {
        binding.checkBoxTestFragmentAnswerA.isChecked = answer.a
        binding.checkBoxTestFragmentAnswerB.isChecked = answer.b
        binding.checkBoxTestFragmentAnswerC.isChecked = answer.c
        binding.checkBoxTestFragmentAnswerD.isChecked = answer.d
        binding.checkBoxTestFragmentAnswerE.isChecked = answer.e
    }

    private fun setInfo (binding: TestFragmentBinding) {
        val u : UserInformation = Gson().fromJson(viewModel.info.value, UserInformation::class.java)
        binding.textViewTestFragmentName.text = u.name
        binding.textViewTestFragmentSurname.text = u.surname
        binding.textViewTestFragmentEmail.text = u.email
    }

    private fun clearTicks (binding: TestFragmentBinding) {
        binding.checkBoxTestFragmentAnswerA.isChecked = false
        binding.checkBoxTestFragmentAnswerB.isChecked = false
        binding.checkBoxTestFragmentAnswerC.isChecked = false
        binding.checkBoxTestFragmentAnswerD.isChecked = false
        binding.checkBoxTestFragmentAnswerE.isChecked = false
    }
}
