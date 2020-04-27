package com.uj.bachelor_jlk700.examsystem.screens.End

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation

import com.uj.bachelor_jlk700.examsystem.R
import com.uj.bachelor_jlk700.examsystem.databinding.EndFragmentBinding

class EndFragment : Fragment() {

    companion object {
        fun newInstance() = EndFragment()
    }

    private lateinit var viewModel: EndViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val ass = EndFragmentArgs.fromBundle(arguments!!).answeredTest
        val binding: EndFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.end_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(EndViewModel::class.java)

        binding.textViewEndFragmentWelcome.text = ass

        binding.buttonEndFragmentBack.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_endFragment_to_startFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // clearPrefs
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        preferences.edit().clear().apply()
        preferences.edit().putString("dontSaveMsg", "dontSaveMsg").apply()
    }
}
