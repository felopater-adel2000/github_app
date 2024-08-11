package com.felo.github_app.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.felo.github_app.R
import com.felo.github_app.databinding.MainToolbarBinding

fun AppCompatActivity.setUpToolBar(
    toolbarBinding: MainToolbarBinding,
    title: String? = "",
    isHome: Boolean = false,
) {
    if (supportActionBar == null) {
        setSupportActionBar(toolbarBinding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (isHome)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        else
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(
            ContextCompat.getDrawable(
                this,
                    R.drawable.ic_back
            )
        )

        if (title.isNullOrEmpty()) {
            toolbarBinding.tvTitle.visibility = View.GONE
        } else {
            toolbarBinding.tvTitle.visibility = View.VISIBLE
            toolbarBinding.tvTitle.text = title
        }
    }
    toolbarBinding.root.setNavigationOnClickListener {
        if (!isHome) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> = this ?: arrayListOf()

fun String?.orDefault(default: String = ""): String = this ?: default

fun Int?.orDefault(default: Int = 0): Int = this ?: default

fun Long?.orDefault(default: Long = 0L): Long = this ?: default

fun Float?.orDefault(default: Float = 0f): Float = this ?: default

fun Double?.orDefault(default: Double = 0.0): Double = this ?: default

fun Boolean?.orDefault(default: Boolean = false): Boolean = this ?: default

fun <T> T?.orDefault(default: T): T = this ?: default

fun Activity.displayToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(@StringRes message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.showSoftKeyboard()
{
    if (currentFocus != null)
    {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Activity.hideSoftKeyboard()
{
    if (currentFocus != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun Activity.displayErrorDialog(message: String?) {

    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = message)
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.MaterialDialog)
        }
}

fun Activity.displayErrorDialog(message: Int?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(res = message)
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.MaterialDialog)
        }
}

fun Activity.displaySuccessDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
            setTheme(R.style.MaterialDialog)
        }
}

fun Activity.displaySuccessDialog(message: Int?, onDismiss: () -> Unit = {}) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(res = message)
            positiveButton(R.string.text_ok)
            setTheme(R.style.MaterialDialog)
            icon(R.mipmap.ic_launcher)
            onDismiss { onDismiss.invoke() }
        }
}