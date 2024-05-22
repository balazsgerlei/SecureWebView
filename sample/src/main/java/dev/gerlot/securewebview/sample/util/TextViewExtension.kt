package dev.gerlot.securewebview.sample.util

import android.view.KeyEvent
import android.widget.TextView

fun TextView.setOnDoneActionListener(onDoneAction: () -> Boolean) =
    setOnEditorActionListener { _, actionId, event ->
        if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENDCALL
            || event?.keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            onDoneAction()
            hideKeyboard()
        }
        false
    }
