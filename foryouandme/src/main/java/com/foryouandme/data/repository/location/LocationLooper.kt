package com.foryouandme.data.repository.location

import android.os.HandlerThread
import android.os.Looper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationLooper @Inject constructor() {

    private val handlerThread = HandlerThread("LocationHandlerThread")

    init {

        handlerThread.start()

    }

    val looper: Looper = handlerThread.looper

}