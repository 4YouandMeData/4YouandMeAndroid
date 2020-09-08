package org.fouryouandme.researchkit.result.logger

import arrow.core.Either
import arrow.fx.IO
import arrow.fx.extensions.fx
import java.io.FileOutputStream

/**
 *
 * The DataLogger on object can be used to stream data to a file on another thread
 * It should only be used when the data may become too large to store in app memory
 *
 */
object DataLogger {

    private const val UTF_8 = "UTF-8"

    fun write(
        fileOutputStream: FileOutputStream,
        data: String
    ): IO<Either<Throwable, Unit>> =
        IO.fx { !fileOutputStream.writeIO(data).attempt() }

    fun write(
        fileOutputStream: FileOutputStream,
        byteArray: ByteArray
    ): IO<Either<Throwable, Unit>> =
        IO.fx { !fileOutputStream.writeIO(byteArray).attempt() }

    private fun FileOutputStream.writeIO(data: String): IO<Unit> =
        writeIO(data.toByteArray(charset(UTF_8)))

    private fun FileOutputStream.writeIO(byteArray: ByteArray): IO<Unit> =
        IO.fx { write(byteArray) }

}