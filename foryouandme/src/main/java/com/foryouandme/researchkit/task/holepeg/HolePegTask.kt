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
    holePegDescriptionStartCenter: String?,
    holePegDescriptionEndCenter: String?,
    holePegDescriptionStart: String?,
    holePegDescriptionEnd: String?,
    holePegDescriptionGrab: String?,
    holePegDescriptionRelease: String?,
    holePegDescriptionColor: Int,
    holePegProgressColor: Int,
    holePegPointColor: Int,
    holePegTargetColor: Int,
) : Task(TaskIdentifiers.HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        getHolePegCoreSteps(
            holePegBackgroundColor = holePegBackgroundColor,
            holePegTitle = holePegTitle,
            holePegTitleColor = holePegTitleColor,
            holePegDescriptionStartCenter = holePegDescriptionStartCenter,
            holePegDescriptionEndCenter = holePegDescriptionEndCenter,
            holePegDescriptionStart = holePegDescriptionStart,
            holePegDescriptionEnd = holePegDescriptionEnd,
            holePegDescriptionGrab = holePegDescriptionGrab,
            holePegDescriptionRelease = holePegDescriptionRelease,
            holePegDescriptionColor = holePegDescriptionColor,
            holePegProgressColor = holePegProgressColor,
            holePegPointColor = holePegPointColor,
            holePegTargetColor = holePegTargetColor
        )

    }


    companion object {

        const val HOLE_PEG: String = "hole_peg"

        fun getHolePegCoreSteps(
            holePegBackgroundColor: Int,
            holePegTitle: String?,
            holePegTitleColor: Int,
            holePegDescriptionStartCenter: String?,
            holePegDescriptionEndCenter: String?,
            holePegDescriptionStart: String?,
            holePegDescriptionEnd: String?,
            holePegDescriptionGrab: String?,
            holePegDescriptionRelease: String?,
            holePegDescriptionColor: Int,
            holePegProgressColor: Int,
            holePegPointColor: Int,
            holePegTargetColor: Int,
            holePegSubSteps: List<HolePegSubStep> = getHolePegDefaultSubSteps()
        ): List<Step> =

            listOf(
                HolePegStep(
                    identifier = HOLE_PEG,
                    backgroundColor = holePegBackgroundColor,
                    title = holePegTitle,
                    titleColor = holePegTitleColor,
                    descriptionStartCenter = holePegDescriptionStartCenter,
                    descriptionEndCenter = holePegDescriptionEndCenter,
                    descriptionStart = holePegDescriptionStart,
                    descriptionEnd = holePegDescriptionEnd,
                    descriptionGrab = holePegDescriptionGrab,
                    descriptionRelease = holePegDescriptionRelease,
                    descriptionColor = holePegDescriptionColor,
                    progressColor = holePegProgressColor,
                    pointColor = holePegPointColor,
                    targetColor = holePegTargetColor,
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
                    HolePegTargetPosition.End
                ),
                HolePegSubStep(
                    HolePegPointPosition.Start,
                    HolePegTargetPosition.EndCenter
                ),
                HolePegSubStep(
                    HolePegPointPosition.End,
                    HolePegTargetPosition.Start
                ),
            )

    }

}