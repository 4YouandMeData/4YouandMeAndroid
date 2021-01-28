package com.foryouandme.core.ext

import kotlinx.coroutines.Job

val Job?.isTerminated: Boolean
    get() = this == null || !isActive || isCancelled || isCompleted