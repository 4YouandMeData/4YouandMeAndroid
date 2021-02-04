package com.foryouandme.researchkit.recorder.sensor.json

import android.content.Context
import arrow.fx.typeclasses.Disposable
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.core.ext.mapNull
import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.logger.DataLogger
import com.foryouandme.researchkit.step.Step
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
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

    private val file = File(outputDirectory, uniqueFilename + JSON_FILE_SUFFIX)
    private var fileOutputStream: FileOutputStream? = null

    override suspend fun start(context: Context) {

        startTime = 0L
        endTime = null

        startJsonDataLogging()

    }

    private suspend fun startJsonDataLogging() {

        isRecording = true
        isFirstJsonObject = true

        openStream()?.let { DataLogger.write(it, "[") }

    }

    override suspend fun stop(): FileResult? {

        endTime = System.currentTimeMillis()

        return stopJsonDataLogging()

    }

    private suspend fun stopJsonDataLogging(): FileResult? =
        withContext(Dispatchers.IO) {

            isRecording = false

            openStream()?.let { DataLogger.write(it, "]") }

            FileResult(
                fileResultIdentifier(),
                file,
                JSON_MIME_CONTENT_TYPE,
                Instant.ofEpochMilli(startTime ?: 0).atZone(ZoneOffset.UTC),
                Instant.ofEpochMilli(endTime ?: 0).atZone(ZoneOffset.UTC)
            )

        }

    suspend fun writeJsonObjectToFile(json: String) {
        withContext(Dispatchers.IO) {

            if (isRecording) {

                // append optional comma for array separation
                val jsonSeparator = if (!isFirstJsonObject) JSON_OBJECT_SEPARATOR else ""

                val jsonString = "$jsonSeparator${json}"

                openStream()?.let { DataLogger.write(it, jsonString) }

                isFirstJsonObject = false

            }
        }

    }

    private suspend fun openStream(): FileOutputStream? =
        withContext(Dispatchers.IO) {
            catchToNull {

                if (outputDirectory.exists().not())
                    outputDirectory.mkdirs()

                if (file.exists().not()) file.createNewFile()

                fileOutputStream.mapNull {

                    val fos = FileOutputStream(file, true)
                    fileOutputStream = fos
                    fos

                }

            }
        }

    private suspend fun deleteFileAndClose() {
        withContext(Dispatchers.IO) {
            catchToNull {
                file.delete()
                fileOutputStream?.close()
            }
        }
    }

    override suspend fun cancel() {

        deleteFileAndClose()

    }

    companion object {

        const val JSON_MIME_CONTENT_TYPE = "application/json"
        const val JSON_FILE_SUFFIX = ".json"
        const val JSON_OBJECT_SEPARATOR = ","

    }
}