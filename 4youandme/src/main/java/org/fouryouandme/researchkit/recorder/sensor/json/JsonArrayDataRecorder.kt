package org.fouryouandme.researchkit.recorder.sensor.json

import arrow.core.Either
import arrow.core.flatMap
import arrow.fx.coroutines.evalOn
import arrow.fx.typeclasses.Disposable
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.ext.evalOnIO
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.mapNull
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.logger.DataLogger
import org.fouryouandme.researchkit.step.Step
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import java.io.File
import java.io.FileOutputStream

/**
 *
 * The JsonArrayDataRecorder class is set up to be able to save a JsonArray to a file
 * It coordinates the file header and footer of "[" and "]" and injects a separator ","
 * in between individual json object writes, so that the format of the file is correct
 */
abstract class JsonArrayDataRecorder(
    identifier: String,
    step: Step,
    outputDirectory: File,
) : Recorder(identifier, step, outputDirectory) {

    private var isFirstJsonObject = false

    private var writing: Disposable? = null
    private val file = File(outputDirectory, uniqueFilename + JSON_FILE_SUFFIX)
    private var fileOutputStream: FileOutputStream? = null

    suspend fun startJsonDataLogging(): Unit {

        isRecording = true
        isFirstJsonObject = true

        val write =
            evalOnIO { openStream().flatMap { DataLogger.write(it, "[") } }

        if (write is Either.Left)
            evalOnMain { recorderListener?.onFail(this, write.a) }

    }

    private suspend fun stopJsonDataLogging(): Unit {
        evalOnIO {
            isRecording = false

            val write =
                openStream().flatMap { DataLogger.write(it, "]") }

            when (write) {

                is Either.Left ->
                    evalOnMain { recorderListener?.onFail(this, write.a) }
                is Either.Right -> {

                    val fileResult =
                        FileResult(
                            fileResultIdentifier(),
                            file,
                            JSON_MIME_CONTENT_TYPE,
                            Instant.ofEpochMilli(startTime ?: 0).atZone(ZoneOffset.UTC),
                            Instant.ofEpochMilli(endTime ?: 0).atZone(ZoneOffset.UTC)
                        )


                    //TODO: Handle result
                    //it.onComplete(this@JsonArrayDataRecorder, fileResult)

                }
            }
        }

    }

    suspend fun writeJsonObjectToFile(json: String): Unit {

        evalOnIO {

            if (isRecording) {

                // append optional comma for array separation
                val jsonSeparator = if (!isFirstJsonObject) JSON_OBJECT_SEPARATOR else ""

                val jsonString = "$jsonSeparator${json}"

                val write =
                    openStream()
                        .flatMap { DataLogger.write(it, jsonString) }

                when (write) {

                    is Either.Left ->
                        evalOnMain {
                            recorderListener?.onFail(this@JsonArrayDataRecorder, write.a)
                        }
                    is Either.Right ->
                        isFirstJsonObject = false
                }
            }
        }

    }

    private suspend fun openStream(): Either<Throwable, FileOutputStream> =
        Either.catch {

            evalOn(Dispatchers.IO) {

                if (!file.exists()) file.createNewFile()

                fileOutputStream.mapNull {

                    val fos = FileOutputStream(file, true)
                    fileOutputStream = fos
                    fos

                }

            }
        }

    private suspend fun deleteFileAndClose(): Unit {

        Either.catch {

            evalOn(Dispatchers.IO) {
                file.delete()
                fileOutputStream?.close()
            }
        }

    }

    override suspend fun cancel(): Unit {

        writing?.invoke()
        deleteFileAndClose()

    }

    override suspend fun stop(): Unit {

        stopJsonDataLogging()

    }

    companion object {

        const val JSON_MIME_CONTENT_TYPE = "application/json"
        const val JSON_FILE_SUFFIX = ".json"
        const val JSON_OBJECT_SEPARATOR = ","

    }
}