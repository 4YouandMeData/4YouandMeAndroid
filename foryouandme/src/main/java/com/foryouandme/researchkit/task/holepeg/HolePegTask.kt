package com.foryouandme.researchkit.task.holepeg

import com.foryouandme.R
import com.foryouandme.core.ext.toTextResource
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.holepeg.HolePegStep
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.fitness.FitnessTask

class HolePegTask(
    id: String,
    introBackImage: Int,
    introBackgroundColor: Int,
    introTitle: String?,
    introTitleColor: Int,
    introDescription: String?,
    introDescriptionColor: Int,
    introImage: Int,
    introButton: String?,
    introButtonColor: Int,
    introButtonTextColor: Int,
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
            introBackImage = introBackImage,
            introBackgroundColor = introBackgroundColor,
            introTitle = introTitle,
            introTitleColor = introTitleColor,
            introDescription = introDescription,
            introDescriptionColor = introDescriptionColor,
            introImage = introImage,
            introButton = introButton,
            introButtonColor = introButtonColor,
            introButtonTextColor = introButtonTextColor,
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

        const val HOLE_PEG_INTRO: String = "hole_peg_intro"

        const val HOLE_PEG: String = "hole_peg"

        fun getHolePegCoreSteps(
            introBackImage: Int,
            introBackgroundColor: Int,
            introTitle: String?,
            introTitleColor: Int,
            introDescription: String?,
            introDescriptionColor: Int,
            introImage: Int?,
            introButton: String?,
            introButtonColor: Int,
            introButtonTextColor: Int,
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
            holePegSubSteps: List<HolePegSubStep> = getHolePegDefaultSubSteps(),
            holePegNumberOfPegs: Int = 9
        ): List<Step> =

            listOf(
                IntroductionStep(
                    identifier = HOLE_PEG_INTRO,
                    back = Back(introBackImage),
                    backgroundColor = introBackgroundColor,
                    title = introTitle.toTextResource(R.string.HOLE_PEG_title),
                    titleColor = introTitleColor,
                    description =
                    introDescription.toTextResource(
                        R.string.HOLE_PEG_intro_description,
                        holePegNumberOfPegs.toString()
                    ),
                    descriptionColor = introDescriptionColor,
                    image = introImage ?: R.drawable.hole_peg_intro,
                    button = introButton.toTextResource(R.string.TASK_next),
                    buttonColor = introButtonColor,
                    buttonTextColor = introButtonTextColor,
                ),
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
                )
            )

    }

}