package com.foryouandme.core.researchkit.step.page

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step

class FYAMPageStep(
    identifier: String,
    back: Back,
    val configuration: Configuration,
    val page: Page,
    val pageType: EPageType,
    val remind: Boolean
) : Step(identifier, back, null, { FYAMPageStepFragment() })