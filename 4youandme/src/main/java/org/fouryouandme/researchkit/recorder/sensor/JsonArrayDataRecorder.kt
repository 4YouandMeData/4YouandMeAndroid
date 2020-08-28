package org.fouryouandme.researchkit.recorder.sensor

import arrow.core.*
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.typeclasses.Disposable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.ext.accumulateError
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.logger.DataLogger
import org.fouryouandme.researchkit.step.Step
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
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
    private val moshi: Moshi
) : Recorder(identifier, step, outputDirectory) {

    private var isFirstJsonObject = false
    private var startTime: Long = 0
    private var endTime: Long = 0

    private var writing: Option<Disposable> = None
    private val file = File(outputDirectory, uniqueFilename + JSON_FILE_SUFFIX)
    private var fileOutputStream: Option<FileOutputStream> = None

    fun startJsonDataLogging(): Unit =
        IO.fx {

            isRecording = true
            startTime = System.currentTimeMillis()
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

    private fun stopJsonDataLogging(): Unit =
        IO.fx {

            isRecording = false
            endTime = System.currentTimeMillis()

            val write =
                openStream()
                    .attempt()
                    .bind()
                    .map { DataLogger.write(it, "]") }
                    .accumulateError(fx)
                    .bind()


            continueOn(Dispatchers.Main)
            when (write) {

                is Either.Left ->
                    recorderListener.map {
                        it.onFail(this@JsonArrayDataRecorder, write.a)
                    }
                is Either.Right -> {

                    val fileResult =
                        FileResult(fileResultIdentifier(), file, JSON_MIME_CONTENT_TYPE)

                    fileResult.startDate = Date(startTime)
                    fileResult.endDate = Date(endTime)

                    recorderListener.map {
                        it.onComplete(this@JsonArrayDataRecorder, fileResult)
                    }

                }

            }


        }.unsafeRunAsync()

    fun writeJsonObjectToFile(json: Map<String, Any>): Unit =
        IO.fx {

            // append optional comma for array separation
            val jsonSeparator = if (!isFirstJsonObject) JSON_OBJECT_SEPARATOR else ""

            val type: Type =
                Types.newParameterizedType(
                    Map::class.java,
                    String::class.java,
                    String::class.java
                )

            val adapter: JsonAdapter<Map<String, Any>> = moshi.adapter(type)

            val data = json.mapValues { it.value.toString() }

            val jsonString = "$jsonSeparator${adapter.toJson(data)}"

            val write =
                openStream()
                    .attempt()
                    .bind()
                    .map { DataLogger.write(it, jsonString) }
                    .accumulateError(fx)
                    .bind()
                    .map { }

            continueOn(Dispatchers.Main)
            when (write) {

                is Either.Left ->
                    recorderListener.map {
                        it.onFail(this@JsonArrayDataRecorder, write.a)
                    }
                is Either.Right ->
                    isFirstJsonObject = false
            }

        }.unsafeRunAsync()

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

        stopJsonDataLogging()
    }

    companion object {

        const val JSON_MIME_CONTENT_TYPE = "application/json"
        const val JSON_FILE_SUFFIX = ".json"
        const val JSON_OBJECT_SEPARATOR = ","

    }
}