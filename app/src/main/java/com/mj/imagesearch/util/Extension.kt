package com.mj.imagesearch.util

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import com.google.android.material.tabs.TabLayout

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun fromHtml(source: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(source)
    }
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun logWithThreadAndCoroutineInfo(message: String) =
    println("[${Thread.currentThread().name}] $message")

fun addCoroutineDebugInfo(message: String) = "[${Thread.currentThread().name}] $message"

@Suppress("INACCESSIBLE_TYPE")
fun TabLayout.Tab.setTypeface(tf: Typeface?, style: Int) {
    val text = (view as? ViewGroup)?.get(1) as? TextView ?: return
    text.setTypeface(tf ?: text.typeface, style)
}

/**
 * From kotlinextensions.com
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}
