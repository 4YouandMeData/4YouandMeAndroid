package com.fouryouandme.researchkit.recorder

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

}