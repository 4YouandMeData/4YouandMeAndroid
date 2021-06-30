package com.foryouandme.researchkit.task.holepeg

import com.foryouandme.R
import com.foryouandme.core.ext.toTextSource
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.holepeg.HolePegStep
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

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
    tutorialBackImage: Int,
    tutorialBackgroundColor: Int,
    tutorialTitle: String?,
    tutorialTitleColor: Int,
    tutorialDescription: String?,
    tutorialDescriptionColor: Int,
    tutorialImage: Int,
    tutorialButton: String?,
    tutorialButtonColor: Int,
    tutorialButtonTextColor: Int,
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
            tutorialBackImage = tutorialBackImage,
            tutorialBackgroundColor = tutorialBackgroundColor,
            tutorialTitle = tutorialTitle,
            tutorialTitleColor = tutorialTitleColor,
            tutorialDescription = tutorialDescription,
            tutorialDescriptionColor = tutorialDescriptionColor,
            tutorialImage = tutorialImage,
            tutorialButton = tutorialButton,
            tutorialButtonColor = tutorialButtonColor,
            tutorialButtonTextColor = tutorialButtonTextColor,
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

        const val HOLE_PEG_TUTORIAL: String = "hole_peg_tutorial"

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
            tutorialBackImage: Int,
            tutorialBackgroundColor: Int,
            tutorialTitle: String?,
            tutorialTitleColor: Int,
            tutorialDescription: String?,
            tutorialDescriptionColor: Int,
            tutorialImage: Int?,
            tutorialButton: String?,
            tutorialButtonColor: Int,
            tutorialButtonTextColor: Int,
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
                    title = introTitle.toTextSource(R.string.HOLE_PEG_title),
                    titleColor = introTitleColor,
                    description =
                    introDescription.toTextSource(
                        R.string.HOLE_PEG_intro_description,
                        holePegNumberOfPegs.toString()
                    ),
                    descriptionColor = introDescriptionColor,
                    image = listOf(introImage ?: R.drawable.hole_peg_intro),
                    button = introButton.toTextSource(R.string.TASK_next),
                    buttonColor = introButtonColor,
                    buttonTextColor = introButtonTextColor,
                ),
                IntroductionStep(
                    identifier = HOLE_PEG_TUTORIAL,
                    back = Back(tutorialBackImage),
                    backgroundColor = tutorialBackgroundColor,
                    title = tutorialTitle.toTextSource(R.string.HOLE_PEG_title),
                    titleColor = tutorialTitleColor,
                    description =
                    tutorialDescription.toTextSource(
                        R.string.HOLE_PEG_tutorial_description,
                        holePegNumberOfPegs.toString()
                    ),
                    descriptionColor = tutorialDescriptionColor,
                    image =
                    if (tutorialImage != null) listOf(tutorialImage)
                    else listOf(
                        R.drawable.hole_peg_tutorial_1,
                        R.drawable.hole_peg_tutorial_2,
                        R.drawable.hole_peg_tutorial_3,
                        R.drawable.hole_peg_tutorial_4,
                        R.drawable.hole_peg_tutorial_5,
                        R.drawable.hole_peg_tutorial_6
                    ),
                    button = tutorialButton.toTextSource(R.string.TASK_next),
                    buttonColor = tutorialButtonColor,
                    buttonTextColor = tutorialButtonTextColor,
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