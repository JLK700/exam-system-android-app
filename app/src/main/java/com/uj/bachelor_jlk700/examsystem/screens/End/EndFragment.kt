package com.uj.bachelor_jlk700.examsystem.screens.End

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.uj.bachelor_jlk700.examsystem.R
import com.uj.bachelor_jlk700.examsystem.databinding.EndFragmentBinding
import com.uj.bachelor_jlk700.examsystem.databinding.TestFragmentBinding
import timber.log.Timber

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

        binding.textViewEndFragmentWelcome.text = ass

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EndViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
