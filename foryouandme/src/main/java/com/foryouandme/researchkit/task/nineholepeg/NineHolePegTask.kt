package com.foryouandme.researchkit.task.nineholepeg

import com.foryouandme.entity.task.nineholepeg.NineHolePegPointPosition
import com.foryouandme.entity.task.nineholepeg.NineHolePegSubStep
import com.foryouandme.entity.task.nineholepeg.NineHolePegTargetPosition
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.nineholepeg.NineHolePegStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class NineHolePegTask(
    id: String,
    nineHolePegBackgroundColor: Int,
    nineHolePegTitle: String?,
    nineHolePegTitleColor: Int,
    nineHolePegDescriptionShape: String?,
    nineHolePegDescriptionGrab: String?,
    nineHolePegDescriptionRelease: String?,
    nineHolePegDescriptionColor: Int,
) : Task(TaskIdentifiers.NINE_HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        getNineHolePegCoreSteps(
            nineHolePegBackgroundColor = nineHolePegBackgroundColor,
            nineHolePegTitle = nineHolePegTitle,
            nineHolePegTitleColor = nineHolePegTitleColor,
            nineHolePegDescriptionShape = nineHolePegDescriptionShape,
            nineHolePegDescriptionGrab = nineHolePegDescriptionGrab,
            nineHolePegDescriptionRelease = nineHolePegDescriptionRelease,
            nineHolePegDescriptionColor = nineHolePegDescriptionColor,
        )

    }


    companion object {

        const val NINE_HOLE_PEG: String = "nine_hole_peg"

        fun getNineHolePegCoreSteps(
            nineHolePegBackgroundColor: Int,
            nineHolePegTitle: String?,
            nineHolePegTitleColor: Int,
            nineHolePegDescriptionShape: String?,
            nineHolePegDescriptionGrab: String?,
            nineHolePegDescriptionRelease: String?,
            nineHolePegDescriptionColor: Int,
            nineHolePegSubSteps: List<NineHolePegSubStep> = getNineHolePegDefaultSubSteps()
        ): List<Step> =

            listOf(
                NineHolePegStep(
                    identifier = NINE_HOLE_PEG,
                    backgroundColor = nineHolePegBackgroundColor,
                    title = nineHolePegTitle,
                    titleColor = nineHolePegTitleColor,
                    descriptionShape = nineHolePegDescriptionShape,
                    descriptionGrab = nineHolePegDescriptionGrab,
                    descriptionRelease = nineHolePegDescriptionRelease,
                    descriptionColor = nineHolePegDescriptionColor,
                    subSteps = nineHolePegSubSteps,
                )
            )

        private fun getNineHolePegDefaultSubSteps(): List<NineHolePegSubStep> =
            listOf(
                NineHolePegSubStep(
                    NineHolePegPointPosition.End,
                    NineHolePegTargetPosition.StartCenter
                ),
                NineHolePegSubStep(
                    NineHolePegPointPosition.Start,
                    NineHolePegTargetPosition.EndCenter
                )
            )

    }

}