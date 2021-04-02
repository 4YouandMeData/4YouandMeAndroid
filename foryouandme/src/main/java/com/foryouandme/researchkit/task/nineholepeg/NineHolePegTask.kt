package com.foryouandme.researchkit.task.nineholepeg

import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.nineholepeg.NineHolePegStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class NineHolePegTask(
    id: String
) : Task(TaskIdentifiers.NINE_HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        getNineHolePegCoreSteps()

    }


    companion object {

        const val NINE_HOLE_PEG: String = "nine_hole_peg"

        fun getNineHolePegCoreSteps(): List<Step> =

            listOf(
                NineHolePegStep(
                    NINE_HOLE_PEG,
                )
            )

    }

}