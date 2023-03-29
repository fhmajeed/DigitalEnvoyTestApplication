package com.example.digitalenvoy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.example.digitalenvoy.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            idTextView.setText(getString(R.string.helloworld))
            val helloAsLink = "Hello"
            idTextView.addLink(Pair(helloAsLink,View.OnClickListener {
                helloAsLink.showToast()
            }))
        }
    }

    private fun String.showToast() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_LONG).show()
    }
}

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

        if (startIndex == -1) {
            spanningString.setSpan(
                clickableSpan,
                startIndex,
                startIndex + txt.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanningString, TextView.BufferType.SPANNABLE)
}