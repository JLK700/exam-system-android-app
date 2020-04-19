package com.uj.bachelor_jlk700.examsystem.screens.Start

import android.widget.Toast
import androidx.lifecycle.ViewModel
import timber.log.Timber

class StartViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Timber.i("TEST MSG")
    }

    fun sayHello () {
        Timber.i("HELLO")
    }
}
