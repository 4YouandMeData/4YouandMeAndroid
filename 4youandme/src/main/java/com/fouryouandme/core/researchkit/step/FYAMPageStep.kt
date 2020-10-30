package com.fouryouandme.core.researchkit.step

import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.page.Page
import com.fouryouandme.core.view.page.EPageType
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Step

class FYAMPageStep(
    identifier: String,
    back: Back,
    val configuration: Configuration,
    val page: Page,
    val pageType: EPageType,
) : Step(identifier, back, null, { FYAMPageStepFragment() })