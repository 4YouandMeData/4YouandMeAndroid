package org.fouryouandme.auth.wearable.login

import android.webkit.JavascriptInterface


class WearableLoginInterface(val success: () -> Unit, val failure: () -> Unit) {

    @JavascriptInterface
    fun wearableLogin(value: String) {

        when (value) {
            "success" -> success()
            "failure" -> failure()
            else -> failure()
        }

    }

}