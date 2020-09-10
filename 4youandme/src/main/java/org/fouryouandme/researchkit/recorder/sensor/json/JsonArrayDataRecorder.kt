package org.fouryouandme.researchkit.recorder.sensor.json

import arrow.core.*
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.typeclasses.Disposable
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.ext.accumulateError
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.logger.DataLogger
import org.fouryouandme.researchkit.step.Step
import java.io.File
import java.io.FileOutputStream
import java.util.*

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

    private var writing: Option<Disposable> = None
    private val file = File(outputDirectory, uniqueFilename + JSON_FILE_SUFFIX)
    private var fileOutputStream: Option<FileOutputStream> = None

    fun startJsonDataLogging(): Unit =
        IO.fx {

            isRecording = true
            isFirstJsonObject = true

            val write =
                openStream()
                    .attempt()
                    .bind()
                    .map { DataLogger.write(it, "[") }
                    .accumulateError(fx)
                    .bind()


            continueOn(Dispatchers.Main)
            when (write) {

                is Either.Left ->
                    recorderListener.map {
                        it.onFail(this@JsonArrayDataRecorder, write.a)
                    }
                is Either.Right -> Unit

            }


        }.unsafeRunAsync()

    private fun stopJsonDataLogging(): IO<Unit> =
        IO.fx {

            isRecording = false

            val write =
                openStream()
                    .attempt()
                    .bind()
                    .map { DataLogger.write(it, "]") }
                    .accumulateError(fx)
                    .bind()

            // switch to main thread before invoke the listener
            continueOn(Dispatchers.Main)
            when (write) {

                is Either.Left ->
                    recorderListener.map {
                        it.onFail(this@JsonArrayDataRecorder, write.a)
                    }
                is Either.Right -> {

                    val fileResult =
                        FileResult(fileResultIdentifier(), file, JSON_MIME_CONTENT_TYPE)

                    fileResult.startDate = Date(startTime.getOrElse { 0 })
                    fileResult.endDate = Date(endTime.getOrElse { 0 })

                    recorderListener.map {
                        it.onComplete(this@JsonArrayDataRecorder, fileResult)
                    }

                }
            }
            // return to IO thread
            continueOn(Dispatchers.IO)

        }

    fun writeJsonObjectToFile(json: String): IO<Unit> =
        IO.fx {

            continueOn(Dispatchers.IO)
            // append optional comma for array separation
            val jsonSeparator = if (!isFirstJsonObject) JSON_OBJECT_SEPARATOR else ""

            val jsonString = "$jsonSeparator${json}"

            val write =
                openStream()
                    .attempt()
                    .bind()
                    .map { DataLogger.write(it, jsonString) }
                    .accumulateError(fx)
                    .bind()
                    .map { }

            when (write) {

                is Either.Left ->
                    recorderListener.map {
                        it.onFail(this@JsonArrayDataRecorder, write.a)
                    }
                is Either.Right ->
                    isFirstJsonObject = false
            }

            Unit

        }

    private fun openStream(): IO<FileOutputStream> =
        IO.fx {

            if (!file.exists()) file.createNewFile()

            fileOutputStream.getOrElse {

                val fos = FileOutputStream(file, true)
                fileOutputStream = fos.some()
                fos

            }
        }

    private fun deleteFileAndClose(): IO<Unit> =
        IO.fx {

            !IO.fx {
                file.delete()
                fileOutputStream.map { it.close() }
            }.attempt()

            Unit

        }

    override fun cancel() {

        writing.map { it() }
        deleteFileAndClose().unsafeRunAsync()

    }

    override fun stop() {

        stopJsonDataLogging().unsafeRunAsync()

    }

    companion object {

        const val JSON_MIME_CONTENT_TYPE = "application/json"
        const val JSON_FILE_SUFFIX = ".json"
        const val JSON_OBJECT_SEPARATOR = ","

    }
}