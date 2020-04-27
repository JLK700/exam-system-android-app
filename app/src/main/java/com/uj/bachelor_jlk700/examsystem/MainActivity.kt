package com.uj.bachelor_jlk700.examsystem

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.google.gson.Gson
import timber.log.Timber
import java.time.Instant

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val builder : AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }

        builder?.setMessage("Wanna Quit?")?.setTitle("Warning")

        builder?.apply {
            setPositiveButton("yup") { dialog, id ->
                finishAffinity()
            }
            setNegativeButton("nope") { dialog, id ->
                // nothing
            }
        }
        builder?.create()?.show()
    }
}
