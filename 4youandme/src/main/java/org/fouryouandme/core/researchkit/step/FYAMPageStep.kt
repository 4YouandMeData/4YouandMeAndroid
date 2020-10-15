package org.fouryouandme.core.researchkit.step

import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.step.Step

class FYAMPageStep(
    identifier: String,
    val configuration: Configuration,
    val page: Page,
    val pageType: EPageType
) : Step(identifier, { FYAMPageStepFragment() })