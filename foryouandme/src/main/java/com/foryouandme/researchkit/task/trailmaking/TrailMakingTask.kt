package com.foryouandme.researchkit.task.trailmaking

import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.trailmaking.TrailMakingStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.squareup.moshi.Moshi

class TrailMakingTask(
    id: String,
    trailMakingBackgroundColor: Int,
    ) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        getTrailMakingCoreSteps(trailMakingBackgroundColor = trailMakingBackgroundColor)

    }


    companion object {

        const val TRAIL_MAKING: String = "trail_making"

        fun getTrailMakingCoreSteps(
            trailMakingBackgroundColor: Int,
        ): List<Step> =

            listOf(
                TrailMakingStep(TRAIL_MAKING, trailMakingBackgroundColor)
            )

    }

}