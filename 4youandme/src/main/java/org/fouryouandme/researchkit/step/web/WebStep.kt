package org.fouryouandme.researchkit.step.web

import org.fouryouandme.researchkit.step.Step

class WebStep(
    identifier: String,
    backImage: Int?,
    val backgroundColor: Int,
    val progressBarColor: Int,
    val url: String,
    val cookies: Map<String, String> = emptyMap(),
    val javascriptInterface: WebStepInterface? = null,
    val javascriptInterfaceName: String? = null
) : Step(identifier, null, false, { WebStepFragment() })