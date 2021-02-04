package com.foryouandme.researchkit.result.logger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

/**
 *
 * The DataLogger on object can be used to stream data to a file on another thread
 * It should only be used when the data may become too large to store in app memory
 *
 */
object DataLogger {

    private const val UTF_8 = "UTF-8"

    suspend fun write(
        fileOutputStream: FileOutputStream,
        data: String
    ) {
        fileOutputStream.writeSuspend(data)
    }

    suspend fun write(
        fileOutputStream: FileOutputStream,
        byteArray: ByteArray
    ) {
        fileOutputStream.writeSuspend(byteArray)
    }

    private suspend fun FileOutputStream.writeSuspend(data: String) {
        writeSuspend(data.toByteArray(charset(UTF_8)))
    }

    private suspend fun FileOutputStream.writeSuspend(
        byteArray: ByteArray
    ) {
        // write the bytes on IO dispatcher
        withContext(Dispatchers.IO) { write(byteArray) }

    }

}