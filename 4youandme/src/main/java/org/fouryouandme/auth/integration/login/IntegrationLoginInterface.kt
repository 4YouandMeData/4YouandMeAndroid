package org.fouryouandme.auth.integration.login

import android.webkit.JavascriptInterface


class IntegrationLoginInterface(val success: () -> Unit, val failure: () -> Unit) {

    @JavascriptInterface
    fun wearableLogin(value: String) {

        when (value) {
            "success" -> success()
            "failure" -> failure()
            else -> failure()
        }

    }

}