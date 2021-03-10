package com.foryouandme.researchkit.task.trailmaking

import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.trailmaking.TrailMakingStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class TrailMakingTask(
    id: String,
    trailMakingBackgroundColor: Int,
    trailMakingPointColor: Int,
    trailMakingPointTextColor: Int,
    trailMakingLineTextColor: Int,
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        getTrailMakingCoreSteps(
            trailMakingBackgroundColor = trailMakingBackgroundColor,
            trailMakingPointColor = trailMakingPointColor,
            trailMakingPointTextColor = trailMakingPointTextColor,
            trailMakingLineTextColor = trailMakingLineTextColor
        )

    }


    companion object {

        const val TRAIL_MAKING: String = "trail_making"

        fun getTrailMakingCoreSteps(
            trailMakingBackgroundColor: Int,
            trailMakingPointColor: Int,
            trailMakingPointTextColor: Int,
            trailMakingLineTextColor: Int,
        ): List<Step> =

            listOf(
                TrailMakingStep(
                    TRAIL_MAKING,
                    trailMakingBackgroundColor,
                    trailMakingPointColor,
                    trailMakingPointTextColor,
                    trailMakingLineTextColor
                )
            )

    }

}