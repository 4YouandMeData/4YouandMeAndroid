package org.fouryouandme.researchkit.result.logger

import java.io.File

data class DataWriteListener(
    val onWriteComplete: (File) -> Unit,
    val onWriteError: (Throwable) -> Unit
)
