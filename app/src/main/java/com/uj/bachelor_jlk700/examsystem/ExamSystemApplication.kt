package com.uj.bachelor_jlk700.examsystem

import android.app.Application
import timber.log.Timber

// Current purpose of this class is to make possible using Timber

class ExamSystemApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}