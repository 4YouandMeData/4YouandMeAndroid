package com.foryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime
import java.io.File

/**
 *
 * The `FileResult` class is a result that references the location of a file produced
 * during a task.
 *
 * A file result is typically generated by the library as the task proceeds.
 *
 * Active steps typically produce file results when Accelerometer or Gyroscope sensor data is
 * serialized to disk. Audio recording also produces a file result.
 *
 * When you write a custom step, use files to report results only when the data
 * is likely to be too big to hold in memory for the duration of the task. For
 * example, fitness tasks that use sensors can be quite long and can generate
 * a large number of samples. To compensate for the length of the task,
 * you can stream the samples to disk during the task, and return a `FileResult object
 *
 * @param file
 * The File location of the file produced.
 *
 * It is the responsibility of the receiver of the result object to delete
 * the file when it is no longer needed.
 *
 * The file is typically written to the output directory specified,
 * so it is common to manage the archiving or cleanup of these files by archiving or deleting
 * the entire output directory.
 *
 *@param contentType
 *
 * The MIME content type of the result.
 *
 * For example, "application/json".
 *
 */
class FileResult(
    identifier: String,
    val file: File,
    val contentType: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime
) : StepResult(identifier, startDate, endDate)