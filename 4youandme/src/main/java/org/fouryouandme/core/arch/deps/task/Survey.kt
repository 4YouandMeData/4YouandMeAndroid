package org.fouryouandme.core.arch.deps.task

import arrow.core.extensions.list.foldable.toList
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.survey.Survey
import org.fouryouandme.core.entity.survey.SurveyQuestion
import org.fouryouandme.core.researchkit.step.FYAMPageStep
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.datepicker.DatePickerStep
import org.fouryouandme.researchkit.step.picker.PickerStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.utils.ImageResource
import org.fouryouandme.researchkit.utils.ImageResource.AndroidResource.Companion.toAndroidResource

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
                                    getSurveyStepId(surveyBlock.id, "question_$index"),
                                    configuration.theme.secondaryColor.color(),
                                    question.image?.let { ImageResource.Base64(it) },
                                    question.id,
                                    { question.text },
                                    configuration.theme.primaryTextColor.color(),
                                    configuration.theme.primaryTextColor.color(),
                                    imageConfiguration.signUpNextStep().toAndroidResource()
                                )
                            is SurveyQuestion.Picker ->
                                PickerStep(
                                    getSurveyStepId(surveyBlock.id, "question_$index"),
                                    populateNumericalList(
                                        question.minDisplayValue,
                                        question.maxDisplayValue,
                                        question.minValue!!,
                                        question.maxValue!!
                                    ),
                                    configuration.theme.secondaryColor.color(),
                                    question.image?.let { ImageResource.Base64(it) },
                                    question.id,
                                    { question.text },
                                    configuration.theme.primaryTextColor.color(),
                                    configuration.theme.primaryTextColor.color(),
                                    imageConfiguration.signUpNextStep().toAndroidResource()

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

    var list = mutableListOf(minValue)
    for (i in min..max) list.add(i.toString())
    list.add(maxValue)

    return list.filterNotNull().toList()

}