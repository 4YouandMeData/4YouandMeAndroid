package com.fouryouandme.researchkit.step.web

import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Step

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