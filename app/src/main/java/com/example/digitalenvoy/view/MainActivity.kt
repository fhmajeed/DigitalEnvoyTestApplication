package com.example.digitalenvoy.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.digitalenvoy.databinding.ActivityMainBinding
import com.example.digitalenvoy.viewmodels.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        with(binding) {
            setContentView(root)
            val helloAsLink = "Hello"
            idTextView.addLink(Pair(helloAsLink, View.OnClickListener {
                helloAsLink.showToast()
            }))
        }

        checkLocationPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perm ->
                if (perm[ACCESS_FINE_LOCATION] == true || perm[ACCESS_COARSE_LOCATION] == true) {
                    viewModel.locationSetup()
                }
            }

        checkLocationPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }

    private fun String.showToast() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_LONG).show()
    }
}

/**
 * Task #1 - Triggering action when user taps a single word
 * Added link for keyword base click_listener
 */
private fun TextView.addLink(vararg text: Pair<String, View.OnClickListener>) {
    val spanningString = SpannableString(this.text)
    var startIndex = -1
    for (txt in text) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                Selection.setSelection((p0 as TextView).text as Spannable, 0)
                p0.invalidate()
                txt.second.onClick(p0)
            }

        }
        startIndex = this.text.toString().indexOf(txt.first, startIndex + 1)

        spanningString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + txt.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanningString, TextView.BufferType.SPANNABLE)
}