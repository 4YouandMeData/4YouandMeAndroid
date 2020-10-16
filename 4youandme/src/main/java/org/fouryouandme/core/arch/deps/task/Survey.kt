package org.fouryouandme.core.arch.deps.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.survey.Survey
import org.fouryouandme.core.entity.survey.SurveyQuestion
import org.fouryouandme.core.researchkit.step.FYAMPageStep
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.choosemany.ChooseManyAnswer
import org.fouryouandme.researchkit.step.choosemany.ChooseManyStep
import org.fouryouandme.researchkit.step.chooseone.ChooseOneAnswer
import org.fouryouandme.researchkit.step.chooseone.ChooseOneStep
import org.fouryouandme.researchkit.step.datepicker.DatePickerStep
import org.fouryouandme.researchkit.step.picker.PickerStep
import org.fouryouandme.researchkit.step.range.RangeStep
import org.fouryouandme.researchkit.step.scale.ScaleStep
import org.fouryouandme.researchkit.step.textinput.TextInputStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.utils.ImageResource
import org.fouryouandme.researchkit.utils.ImageResource.AndroidResource.Companion.toAndroidResource
import org.threeten.bp.ZoneOffset

// TODO: handle dynamic task creation
fun buildSurvey(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    survey: Survey
): Task =
    object : Task("survey", id) {

        override val steps: List<Step> by lazy {

            val steps = mutableListOf<Step>()

            survey.surveyBlocks.forEach { surveyBlock ->

                val intro =
                    surveyBlock.introPage.asList().mapIndexed { index, page ->
                        FYAMPageStep(
                            getSurveyStepId(surveyBlock.id, "intro_$index"),
                            configuration,
                            page,
                            EPageType.INFO
                        )
                    }

                val questions =
                    surveyBlock.questions.mapIndexed { index, question ->

                        when (question) {
                            is SurveyQuestion.Date ->
                                DatePickerStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
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
                                PickerStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
                                    values = populateNumericalList(
                                        question.minDisplayValue,
                                        question.maxDisplayValue,
                                        question.minValue,
                                        question.maxValue
                                    ),
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource()

                                )

                            is SurveyQuestion.PickOne ->
                                ChooseOneStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
                                    values = question.answers.map {
                                        ChooseOneAnswer(
                                            it.id,
                                            it.text,
                                            configuration.theme.primaryTextColor.color(),
                                            configuration.theme.primaryColorEnd.color()
                                        )
                                    },
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource()
                                )

                            is SurveyQuestion.PickMany ->
                                ChooseManyStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
                                    values = question.answers.map {
                                        ChooseManyAnswer(
                                            it.id,
                                            it.text,
                                            configuration.theme.primaryTextColor.color(),
                                            configuration.theme.primaryColorEnd.color()
                                        )
                                    },
                                    backgroundColor = configuration.theme.secondaryColor.color(),
                                    image = question.image?.let { ImageResource.Base64(it) },
                                    questionId = question.id,
                                    question = { question.text },
                                    questionColor = configuration.theme.primaryTextColor.color(),
                                    shadowColor = configuration.theme.primaryTextColor.color(),
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource()
                                )

                            is SurveyQuestion.TextInput ->
                                TextInputStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
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
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
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
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource()
                                )

                            is SurveyQuestion.Range ->
                                RangeStep(
                                    identifier = getSurveyStepId(surveyBlock.id, "question_$index"),
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
                                    buttonImage = imageConfiguration.nextStepSecondary()
                                        .toAndroidResource()
                                )
                        }

                    }

                val success =
                    surveyBlock.successPage?.let {

                        FYAMPageStep(
                            getSurveyStepId(surveyBlock.id, "success"),
                            configuration,
                            it,
                            EPageType.SUCCESS
                        )

                    }

                steps.addAll(intro)
                steps.addAll(questions)
                success?.let { steps.add(it) }

            }


            steps

        }

    }

private fun getSurveyStepId(blockId: String, stepId: String): String =
    "survey_block_${blockId}_${stepId}"

private fun populateNumericalList(
    minValue: String?,
    maxValue: String?,
    min: Int,
    max: Int
): List<String> {

    val list = mutableListOf<String>()

    minValue?.let { list.add(it) }

    for (i in min..max) list.add(i.toString())

    maxValue?.let { list.add(it) }

    return list

}