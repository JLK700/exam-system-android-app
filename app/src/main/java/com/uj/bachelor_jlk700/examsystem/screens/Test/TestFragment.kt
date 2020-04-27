package com.uj.bachelor_jlk700.examsystem.screens.Test

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.uj.bachelor_jlk700.examsystem.R
import com.uj.bachelor_jlk700.examsystem.data.*
import com.uj.bachelor_jlk700.examsystem.databinding.TestFragmentBinding

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

        setQuestionCounter(binding)

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
            setQuestionCounter(binding)
        }

        binding.buttonTestFragmentPrevious.setOnClickListener {
            viewModel.saveAnswer(binding.checkBoxTestFragmentAnswerA.isChecked,
                binding.checkBoxTestFragmentAnswerB.isChecked,
                binding.checkBoxTestFragmentAnswerC.isChecked,
                binding.checkBoxTestFragmentAnswerD.isChecked,
                binding.checkBoxTestFragmentAnswerE.isChecked)
            clearTicks(binding)
            viewModel.prevQuestion()
            setQuestionCounter(binding)
        }

        binding.buttonTestFragmentEnd.setOnClickListener {
            val builder : AlertDialog.Builder? = activity.let {
                AlertDialog.Builder(activity)
            }

            builder?.setMessage("Wanna End Test?")?.setTitle("Warning")

            builder?.apply {
                setPositiveButton("yup") { dialog, id ->
                    endTest(binding, it)
                }
                setNegativeButton("nope") { dialog, id ->
                    // nothing
                }
            }
            builder?.create()?.show()
        }

        viewModel.displayTime.observe(viewLifecycleOwner, Observer { displayTime ->
            binding.textViewTestFragmentTimer.text = displayTime
        })

        viewModel.timeLasted.observe(viewLifecycleOwner, Observer {timeToEnd ->
            if (timeToEnd == TimeState.WARNING) {
                Toast.makeText(activity, "Your time is coming to an end", Toast.LENGTH_SHORT).show()
            }

            if (timeToEnd == TimeState.END) {
                endTest(binding, this.view!!)
            }
        })

        if(viewModel.info.value == "restore") {
            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val ans = Gson().fromJson(preferences.getString("interruptedAnsweredTest", ""), AnsweredTest::class.java)

            for (i in 0 until ans.answers.size) {
                viewModel.answers.value!![i] =  ans.answers[i]
            }

            viewModel.answer.value = ans.answers[0]
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

    private fun setQuestionCounter (binding: TestFragmentBinding) {
        binding.textViewTestFragmentQuestionCounter.text =
            "Question: " + (viewModel.currentQuestionIndex.value!!.plus(1)).toString() +
                    " / " + viewModel.test.questions.size.toString()
    }

    private fun clearTicks (binding: TestFragmentBinding) {
        binding.checkBoxTestFragmentAnswerA.isChecked = false
        binding.checkBoxTestFragmentAnswerB.isChecked = false
        binding.checkBoxTestFragmentAnswerC.isChecked = false
        binding.checkBoxTestFragmentAnswerD.isChecked = false
        binding.checkBoxTestFragmentAnswerE.isChecked = false
    }


    private fun endTest (binding: TestFragmentBinding, view: View) {
        viewModel.saveAnswer(binding.checkBoxTestFragmentAnswerA.isChecked,
            binding.checkBoxTestFragmentAnswerB.isChecked,
            binding.checkBoxTestFragmentAnswerC.isChecked,
            binding.checkBoxTestFragmentAnswerD.isChecked,
            binding.checkBoxTestFragmentAnswerE.isChecked)

        val action = TestFragmentDirections.actionTestFragmentToEndFragment()
        action.answeredTest = Gson().toJson(viewModel.generateAnswerTest())
        Navigation.findNavController(view).navigate(action)
    }

    private fun saveTest (test : Test) {
        val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val dontSave : String? = preferences.getString("dontSaveMsg", "nope")

        if (dontSave == "nope") {
            val jsonTest = Gson().toJson(test, Test::class.java)
            val jsonAnswerTest = Gson().toJson(viewModel.generateAnswerTest(), AnsweredTest::class.java)
            val jsonTimeStamp = Gson().toJson(System.currentTimeMillis(), Long::class.java)
            editor.putString("interruptedTest", jsonTest).
            putString("interruptedAnsweredTest", jsonAnswerTest).
            putString("interruptedTimeStamp", jsonTimeStamp).
            apply()
        }
    }

    override fun onStop() {
        super.onStop()
        saveTest(viewModel.test)
    }
}
