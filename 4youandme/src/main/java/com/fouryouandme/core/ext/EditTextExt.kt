package com.fouryouandme.core.ext

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.autoCloseKeyboard(): Unit =
    setOnEditorActionListener { _, actionId, _ ->

        if (actionId == EditorInfo.IME_ACTION_DONE)
            clearFocus()

        false
    }