package  com.uj.bachelor_jlk700.examsystem.screens.Start

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.uj.bachelor_jlk700.examsystem.R
import com.uj.bachelor_jlk700.examsystem.data.UserInformation
import com.uj.bachelor_jlk700.examsystem.databinding.StartFragmentBinding


class StartFragment : Fragment() {

    companion object {
        fun newInstance() =
            StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: StartFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.start_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        binding.button2.setOnClickListener { view: View ->
            val info = UserInformation(
                binding.editTextStartFragmentName.text.toString(),
                binding.editTextStartFragmentSurname.text.toString(),
                binding.editTextStartFragmentEmail.text.toString()
            )

            val action = StartFragmentDirections.actionStartFragmentToTestFragment()
            action.userInfo = Gson().toJson(info)
            Navigation.findNavController(view).navigate(action)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        preferences.edit().remove("dontSaveMsg").apply()
        val json : String? = preferences.getString("interruptedTimeStamp", "")
        if (json != "") {
            val action = StartFragmentDirections.actionStartFragmentToTestFragment()
            action.userInfo = "restore"
            Navigation.findNavController(view!!).navigate(action)
        }
    }
}
