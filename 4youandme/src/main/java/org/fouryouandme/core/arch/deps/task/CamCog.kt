package org.fouryouandme.core.arch.deps.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task

suspend fun FYAMTaskConfiguration.buildCamCog(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration
): Task =
    object : Task("camcog", id) {
        override val steps: List<Step> =
            listOf()
    }