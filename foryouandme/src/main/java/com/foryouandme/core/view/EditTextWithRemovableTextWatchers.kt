package com.foryouandme.core.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditTextWithRemovableTextWatchers(
    context: Context,
    attrs: AttributeSet?
) : AppCompatEditText(context, attrs) {

    private val listeners = mutableListOf<TextWatcher>()

    override fun addTextChangedListener(watcher: TextWatcher) {
        listeners.add(watcher)
        super.addTextChangedListener(watcher)
    }

    override fun removeTextChangedListener(watcher: TextWatcher) {
        listeners.remove(watcher)
        super.removeTextChangedListener(watcher)
    }

    fun clearTextChangedListeners() {
        for (watcher in listeners) super.removeTextChangedListener(watcher)
        listeners.clear()
    }
}