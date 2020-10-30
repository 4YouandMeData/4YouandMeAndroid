package com.foryouandme.researchkit.step.web

import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step

class WebStep(
    identifier: String,
    back: Back?,
    val backgroundColor: Int,
    val progressBarColor: Int,
    val url: String,
    val cookies: Map<String, String> = emptyMap(),
    val javascriptInterface: WebStepInterface? = null,
    val javascriptInterfaceName: String? = null
) : Step(identifier, back, null, { WebStepFragment() })