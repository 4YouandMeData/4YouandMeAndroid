package com.foryouandme.core.ext.web

import android.webkit.JavascriptInterface


class IntegrationLoginInterface(val success: () -> Unit, val failure: () -> Unit) {

    @JavascriptInterface
    fun integrationLogin(value: String) {

        when (value) {
            "success" -> success()
            "failure" -> failure()
            else -> failure()
        }

    }

    companion object {

        const val INTEGRATION_NAME: String = "Android"

    }

}