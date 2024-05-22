package dev.gerlot.securewebview.sample.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.showKeyboard() = ViewCompat.getWindowInsetsController(this)
    ?.show(WindowInsetsCompat.Type.ime())

fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)
    ?.hide(WindowInsetsCompat.Type.ime())
