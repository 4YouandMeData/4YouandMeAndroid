package org.fouryouandme.researchkit.recorder

import android.content.Context
import org.fouryouandme.researchkit.result.Result

/**
 *
 * The `RecorderListener` interface defines methods that the delegate of an `Recorder` object
 * should use to handle errors and log the completed results.
 *
 * This interface is implemented by the step view; your app should not need to implement it.
 *
 */
interface RecorderListener {

    /**
     * Tells the listener that the recorder has completed with the specified result.
     * Typically, this method is called once when recording is stopped.
     *
     * @param recorder The generating recorder object.
     * @param result   The generated result.
     */
    fun onComplete(
        recorder: Recorder,
        result: Result
    )

    /**
     * Tells the listener that recording failed.
     * Typically, this method is called once when the error occurred.
     *
     * @param recorder The generating recorder object.
     * @param error The error that occurred.
     */
    fun onFail(
        recorder: Recorder,
        error: Throwable
    )

    /**
     * @return a valid Context for the recorder to broadcast status, null if not available
     */
    val broadcastContext: Context?
}