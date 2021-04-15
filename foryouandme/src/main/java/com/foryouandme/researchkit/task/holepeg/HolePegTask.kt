package com.foryouandme.researchkit.task.holepeg

import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.holepeg.HolePegStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class HolePegTask(
    id: String,
    holePegBackgroundColor: Int,
    holePegTitle: String?,
    holePegTitleColor: Int,
    holePegDescriptionShape: String?,
    holePegDescriptionGrab: String?,
    holePegDescriptionRelease: String?,
    holePegDescriptionColor: Int,
    holePegProgressColor: Int
) : Task(TaskIdentifiers.HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        getHolePegCoreSteps(
            holePegBackgroundColor = holePegBackgroundColor,
            holePegTitle = holePegTitle,
            holePegTitleColor = holePegTitleColor,
            holePegDescriptionShape = holePegDescriptionShape,
            holePegDescriptionGrab = holePegDescriptionGrab,
            holePegDescriptionRelease = holePegDescriptionRelease,
            holePegDescriptionColor = holePegDescriptionColor,
            holePegProgressColor = holePegProgressColor
        )

    }


    companion object {

        const val HOLE_PEG: String = "hole_peg"

        fun getHolePegCoreSteps(
            holePegBackgroundColor: Int,
            holePegTitle: String?,
            holePegTitleColor: Int,
            holePegDescriptionShape: String?,
            holePegDescriptionGrab: String?,
            holePegDescriptionRelease: String?,
            holePegDescriptionColor: Int,
            holePegProgressColor: Int,
            holePegSubSteps: List<HolePegSubStep> = getHolePegDefaultSubSteps()
        ): List<Step> =

            listOf(
                HolePegStep(
                    identifier = HOLE_PEG,
                    backgroundColor = holePegBackgroundColor,
                    title = holePegTitle,
                    titleColor = holePegTitleColor,
                    descriptionShape = holePegDescriptionShape,
                    descriptionGrab = holePegDescriptionGrab,
                    descriptionRelease = holePegDescriptionRelease,
                    descriptionColor = holePegDescriptionColor,
                    progressColor = holePegProgressColor,
                    subSteps = holePegSubSteps,
                )
            )

        private fun getHolePegDefaultSubSteps(): List<HolePegSubStep> =
            listOf(
                HolePegSubStep(
                    HolePegPointPosition.End,
                    HolePegTargetPosition.StartCenter
                ),
                HolePegSubStep(
                    HolePegPointPosition.Start,
                    HolePegTargetPosition.EndCenter
                )
            )

    }

}