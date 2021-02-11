package com.foryouandme.core.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T> CoroutineScope.launchSafe(block: suspend () -> T): Job =
    launch(CoroutineExceptionHandler { _, error ->
        Timber.tag("ERROR").e(error.toString())
    }) {

        block()

    }