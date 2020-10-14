package org.fouryouandme.core.ext.web

import android.webkit.JavascriptInterface
import org.fouryouandme.researchkit.step.web.WebStepInterface


class CamCogInterface : WebStepInterface() {

    @JavascriptInterface
    fun integrationLogin(value: String) {

        when (value) {
            "success" -> close()
            "failure" -> close()
            else -> close()
        }

    }

    companion object {

        const val INTEGRATION_NAME: String = "Android"

    }

}