package com.foryouandme.researchkit.utils

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.*

class DebounceTextListener(private val onTextChange: (String) -> Unit) : TextWatcher {

    companion object {
        private const val DEBOUNCE_PERIOD = 1000L
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var typingJob: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        typingJob?.cancel()
        typingJob = coroutineScope.launch {
            delay(DEBOUNCE_PERIOD)
            onTextChange(s?.toString().orEmpty())
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

}