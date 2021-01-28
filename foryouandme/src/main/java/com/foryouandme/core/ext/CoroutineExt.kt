package com.foryouandme.core.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <T> CoroutineScope.launchSafe(block: suspend () -> T): Job =
        launch(CoroutineExceptionHandler { _, _ -> }) {

            block()

        }