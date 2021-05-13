package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.resources.ImageResource
import com.foryouandme.entity.resources.ImageResource.AndroidResource.Companion.toAndroidResource
import com.foryouandme.entity.survey.Survey
import com.foryouandme.entity.survey.SurveyBlock
import com.foryouandme.entity.survey.SurveyQuestion
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.choosemany.ChooseManyAnswer
import com.foryouandme.researchkit.step.choosemany.ChooseManyStep
import com.foryouandme.researchkit.step.chooseone.ChooseOneAnswer
import com.foryouandme.researchkit.step.chooseone.ChooseOneStep
import com.foryouandme.researchkit.step.date.DatePickerStep
import com.foryouandme.researchkit.step.number.NumberRangePickerStep
import com.foryouandme.researchkit.step.range.RangeStep
import com.foryouandme.researchkit.step.scale.ScaleStep
import com.foryouandme.researchkit.step.textinput.TextInputStep
import com.foryouandme.researchkit.task.Task
import org.threeten.bp.ZoneOffset

fun buildSurvey(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    survey: Survey,
    pages: List<Page>,
    welcomePage: Page,
    successPage: Page?,
    reschedule: Reschedule?
): Task =
    object : Task("survey", id) {

        override val steps: List<Step> by lazy {

            val steps = mutableListOf<Step>()

            survey.surveyBlocks.forEach { surveyBlock ->

                val intro =
                    surveyBlock.introPage?.asList(surveyBlock.pages)?.mapIndexed { _, page ->

                        FYAMPageStep(
                            getSurveyBlockIntroStepId(surveyBlock.id, page.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            page,
                            EPageType.INFO,
                            false
                        )

                    } ?: emptyList()

                val questions =
                    surveyBlock.questions.map { question ->

                        when (question) {
                            is SurveyQuestion.Date ->
                                DatePickerStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource(),
                                    minDate = question.minDate?.atStartOfDay(ZoneOffset.UTC)
                                        ?.toInstant()
                                        ?.toEpochMilli(),
                                    maxDate = question.maxDate?.atStartOfDay(ZoneOffset.UTC)
                                        ?.toInstant()
                                        ?.toEpochMilli()
                                )

                            is SurveyQuestion.Numerical ->
                                NumberRangePickerStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    min = question.minValue,
                                    max = question.maxValue,
                                    minDisplayValue = question.minDisplayValue,
                                    maxDisplayValue = question.maxDisplayValue,
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource(),
                                    skips =
                                    question.targets.mapNotNull {

                                        val target =
                                            getSkipTarget(
                                                survey.surveyBlocks,
                                                surveyBlock,
                                                successPage,
                                                it.questionId
                                            )

                                        if (target != null)
                                            SurveySkip.Range(it.min, it.max, target)
                                        else
                                            null
                                    }
                                )

                            is SurveyQuestion.PickOne ->
                                ChooseOneStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    values =
                                    question.answers.map {
                                        ChooseOneAnswer(
                                            it.id,
                                            it.text,
                                            configuration.theme.primaryTextColor.color(),
                                            configuration.theme.primaryColorEnd.color(),
                                            it.isOther,
                                            configuration.text.survey.otherAnswerPlaceholder
                                        )
                                    },
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage =
                                    imageConfiguration
                                        .nextStepSecondary()
                                        .toAndroidResource(),
                                    skips =
                                    question.targets.mapNotNull {

                                        val target =
                                            getSkipTarget(
                                                survey.surveyBlocks,
                                                surveyBlock,
                                                successPage,
                                                it.questionId
                                            )

                                        if (target != null)
                                            SurveySkip.Answer(it.answerId, target)
                                        else
                                            null
                                    }
                                )

                            is SurveyQuestion.PickMany ->
                                ChooseManyStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    values = question.answers.map {
                                        ChooseManyAnswer(
                                            it.id,
                                            it.text,
                                            configuration.theme.primaryTextColor.color(),
                                            configuration.theme.primaryColorEnd.color(),
                                            it.isNone,
                                            it.isOther,
                                            configuration.text.survey.otherAnswerPlaceholder
                                        )
                                    },
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage =
                                    imageConfiguration
                                        .nextStepSecondary()
                                        .toAndroidResource(),
                                    skips =
                                    question.targets.mapNotNull {

                                        val target =
                                            getSkipTarget(
                                                survey.surveyBlocks,
                                                surveyBlock,
                                                successPage,
                                                it.questionId
                                            )

                                        if (target != null)
                                            SurveySkip.Answer(it.answerId, target)
                                        else
                                            null
                                    }
                                )

                            is SurveyQuestion.TextInput ->
                                TextInputStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource(),
                                    textColor = configuration.theme.primaryTextColor.color(),
                                    placeholderColor = configuration.theme.fourthTextColor.color(),
                                    placeholder = question.placeholder,
                                    maxCharacters = question.maxCharacters
                                )

                            is SurveyQuestion.Scale ->
                                ScaleStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    minValue = question.min,
                                    maxValue = question.max,
                                    interval = question.interval ?: 1,
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    progressColor = configuration.theme.primaryColorEnd.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage =
                                    imageConfiguration
                                        .nextStepSecondary()
                                        .toAndroidResource(),
                                    skips =
                                    question.targets.mapNotNull {

                                        val target =
                                            getSkipTarget(
                                                survey.surveyBlocks,
                                                surveyBlock,
                                                successPage,
                                                it.questionId
                                            )

                                        if (target != null)
                                            SurveySkip.Range(it.min, it.max, target)
                                        else
                                            null

                                    }
                                )

                            is SurveyQuestion.Range ->
                                RangeStep(
                                    identifier =
                                    getSurveyQuestionStepId(
                                        surveyBlock.id,
                                        question.id
                                    ),
                                    back = Back(imageConfiguration.backSecondary()),
                                    skip =
                                    Skip(
                                        configuration.text.task.skipButton,
                                        configuration.theme.primaryColorEnd.color()
                                    ),
                                    minValue = question.min,
                                    maxValue = question.max,
                                    valueColor = configuration.theme.primaryTextColor.color(),
                                    minDisplayValue = question.minDisplay,
                                    maxDisplayValue = question.maxDisplay,
                                    minDisplayColor = configuration.theme.primaryTextColor.color(),
                                    maxDisplayColor = configuration.theme.primaryTextColor.color(),
                                    progressColor = configuration.theme.primaryColorEnd.color(),
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage =
                                    imageConfiguration
                                        .nextStepSecondary()
                                        .toAndroidResource(),
                                    skips =
                                    question.targets.mapNotNull {

                                        val target =
                                            getSkipTarget(
                                                survey.surveyBlocks,
                                                surveyBlock,
                                                successPage,
                                                it.questionId
                                            )

                                        if (target != null)
                                            SurveySkip.Range(it.min, it.max, target)
                                        else
                                            null

                                    }
                                )
                        }

                    }

                val success =
                    surveyBlock.successPage?.let {

                        FYAMPageStep(
                            getSurveyBlockSuccessStepId(surveyBlock.id, it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS,
                            false
                        )

                    }

                if (questions.isNotEmpty()) {
                    steps.addAll(intro)
                    steps.addAll(questions)
                    success?.let { steps.add(it) }
                }
            }

            val intro =
                welcomePage.asList(pages).mapIndexed { index, page ->

                    FYAMPageStep(
                        getSurveyIntroStepId(page.id),
                        Back(imageConfiguration.backSecondary()),
                        configuration,
                        page,
                        EPageType.INFO,
                        index == 0 && reschedule.isEnabled()
                    )

                }

            val success =
                successPage?.let {

                    FYAMPageStep(
                        getSurveySuccessStepId(it.id),
                        Back(imageConfiguration.backSecondary()),
                        configuration,
                        it,
                        EPageType.SUCCESS,
                        false
                    )

                }


            steps.addAll(0, intro)
            success?.let { steps.add(it) }

            steps

        }

    }

private fun getSurveyIntroStepId(introId: String): String =
    "survey_intro_${introId}"

private fun getSurveySuccessStepId(successId: String): String =
    "survey_success_${successId}"

private fun getSurveyBlockIntroStepId(blockId: String, introId: String): String =
    "survey_block_${blockId}_intro_${introId}"

private fun getSurveyBlockSuccessStepId(blockId: String, successId: String): String =
    "survey_block_${blockId}_success_${successId}"

private fun getSurveyQuestionStepId(blockId: String, questionId: String): String =
    "survey_block_${blockId}_question_${questionId}"

private fun getSkipTarget(
    surveyBlocks: List<SurveyBlock>,
    block: SurveyBlock,
    success: Page?,
    questionId: String
): SkipTarget? =
    if (questionId == "exit") {

        // try to skip to the success of the block if exist or to the next block

        val successId = block.successPage?.let { getSurveyBlockSuccessStepId(block.id, it.id) }

        if (successId != null) SkipTarget.StepId(successId)
        else {

            val blockIndex = surveyBlocks.indexOfFirst { block.id == it.id }

            val nextBlockIndex = if (blockIndex >= 0) blockIndex + 1 else null

            val nextBlock = nextBlockIndex?.let { surveyBlocks.getOrNull(it) }

            if (nextBlock != null) {

                val nextIntroPage = nextBlock.introPage

                if (nextIntroPage != null)
                    SkipTarget.StepId(getSurveyBlockIntroStepId(nextBlock.id, nextIntroPage.id))
                else {

                    val nextQuestionId =
                        when (val nextQuestion = nextBlock.questions.firstOrNull()) {
                            is SurveyQuestion.Date -> nextQuestion.id
                            is SurveyQuestion.Numerical -> nextQuestion.id
                            is SurveyQuestion.PickMany -> nextQuestion.id
                            is SurveyQuestion.PickOne -> nextQuestion.id
                            is SurveyQuestion.Range -> nextQuestion.id
                            is SurveyQuestion.Scale -> nextQuestion.id
                            is SurveyQuestion.TextInput -> nextQuestion.id
                            null -> null
                        }

                    nextQuestionId?.let {
                        SkipTarget.StepId(getSurveyQuestionStepId(nextBlock.id, it))
                    }

                }

            } else
                if (success != null) SkipTarget.StepId(getSurveySuccessStepId(success.id))
                else SkipTarget.End

        }

    } else SkipTarget.StepId(getSurveyQuestionStepId(block.id, questionId))