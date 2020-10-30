package com.fouryouandme.core.ext

import android.view.View

fun View.setOnClickListenerAsync(block: suspend () -> Unit) =
    setOnClickListener { startCoroutineAsync { block() } }