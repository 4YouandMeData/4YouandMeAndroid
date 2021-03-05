package com.foryouandme.core.researchkit.task

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.trailmaking.TrailMakingTask

class FYAMTrailMakingTask(
    id: String,
    private val configuration: Configuration
) : Task(TaskIdentifiers.TRAIL_MAKING, id) {

    override val steps: List<Step> by lazy {

        val secondary =
            configuration.theme.secondaryColor.color()

        TrailMakingTask.getTrailMakingCoreSteps(trailMakingBackgroundColor = secondary)

    }

}