package dev.gerlot.securewebview.sample.util

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

fun Activity.showKeyboard() = WindowCompat.getInsetsController(window, window.decorView)
    .show(WindowInsetsCompat.Type.ime())

fun Activity.hideKeyboard() = WindowCompat.getInsetsController(window, window.decorView)
    .hide(WindowInsetsCompat.Type.ime())
