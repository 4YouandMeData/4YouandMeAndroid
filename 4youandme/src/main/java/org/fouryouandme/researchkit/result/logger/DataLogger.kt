package org.fouryouandme.researchkit.result.logger

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
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
    ): Either<Throwable, Unit> =
        fileOutputStream.writeSuspend(data)

    suspend fun write(
        fileOutputStream: FileOutputStream,
        byteArray: ByteArray
    ): Either<Throwable, Unit> =
        fileOutputStream.writeSuspend(byteArray)

    private suspend fun FileOutputStream.writeSuspend(data: String): Either<Throwable, Unit> =
        writeSuspend(data.toByteArray(charset(UTF_8)))

    private suspend fun FileOutputStream.writeSuspend(
        byteArray: ByteArray
    ): Either<Throwable, Unit> =
        Either.catch {
            // write the bytes on IO dispatcher
            evalOn(Dispatchers.IO) { write(byteArray) }
        }

}