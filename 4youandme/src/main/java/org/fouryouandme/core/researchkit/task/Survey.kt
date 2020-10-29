package org.fouryouandme.core.researchkit.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.cases.task.TaskUseCase.updateSurvey
import org.fouryouandme.core.data.api.task.request.AnswerUpdateRequest
import org.fouryouandme.core.data.api.task.request.SurveyUpdateRequest
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.survey.Survey
import org.fouryouandme.core.entity.survey.SurveyQuestion
import org.fouryouandme.core.researchkit.step.FYAMPageStep
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.result.MultipleAnswerResult
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.result.SingleIntAnswerResult
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.skip.SurveySkip
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Skip
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.choosemany.ChooseManyAnswer
import org.fouryouandme.researchkit.step.choosemany.ChooseManyStep
import org.fouryouandme.researchkit.step.chooseone.ChooseOneAnswer
import org.fouryouandme.researchkit.step.chooseone.ChooseOneStep
import org.fouryouandme.researchkit.step.date.DatePickerStep
import org.fouryouandme.researchkit.step.number.NumberRangePickerStep
import org.fouryouandme.researchkit.step.range.RangeStep
import org.fouryouandme.researchkit.step.scale.ScaleStep
import org.fouryouandme.researchkit.step.textinput.TextInputStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskHandleResult
import org.fouryouandme.researchkit.utils.ImageResource
import org.fouryouandme.researchkit.utils.ImageResource.AndroidResource.Companion.toAndroidResource
import org.threeten.bp.ZoneOffset

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
                            getSurveyIntroStepId(surveyBlock.id, page.id),
                            Back(imageConfiguration.backSecondary()),
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
                                    question.targets.map {
                                        SurveySkip.Range(
                                            it.min,
                                            it.max,
                                            getSurveyQuestionStepId(surveyBlock.id, it.questionId)
                                        )
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
                                            configuration.theme.primaryColorEnd.color()
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
                                    question.targets.map {
                                        SurveySkip.Answer(
                                            it.answerId,
                                            getSurveyQuestionStepId(surveyBlock.id, it.questionId)
                                        )
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
                                            configuration.theme.primaryColorEnd.color()
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
                                    question.targets.map {
                                        SurveySkip.Answer(
                                            it.answerId,
                                            getSurveyQuestionStepId(surveyBlock.id, it.questionId)
                                        )
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
                                    question.targets.map {
                                        SurveySkip.Range(
                                            it.min,
                                            it.max,
                                            getSurveyQuestionStepId(surveyBlock.id, it.questionId)
                                        )
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
                                    question.targets.map {
                                        SurveySkip.Range(
                                            it.min,
                                            it.max,
                                            getSurveyQuestionStepId(surveyBlock.id, it.questionId)
                                        )
                                    }
                                )
                        }

                    }

                val success =
                    surveyBlock.successPage?.let {

                        FYAMPageStep(
                            getSurveySuccessStepId(surveyBlock.id, it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS
                        )

                    }

                if (questions.isNotEmpty()) {
                    steps.addAll(intro)
                    steps.addAll(questions)
                    success?.let { steps.add(it) }
                }
            }


            steps

        }

    }

private fun getSurveyIntroStepId(blockId: String, introId: String): String =
    "survey_block_${blockId}_intro_${introId}"

private fun getSurveySuccessStepId(blockId: String, successId: String): String =
    "survey_block_${blockId}_success_${successId}"

private fun getSurveyQuestionStepId(blockId: String, questionId: String): String =
    "survey_block_${blockId}_question_${questionId}"

suspend fun FYAMTaskConfiguration.sendSurveyData(
    taskModule: TaskModule,
    taskId: String,
    result: TaskResult
): TaskHandleResult {


    val answers =
        result.results.toList().mapNotNull {
            when (val value = it.second) {
                is SingleAnswerResult ->
                    AnswerUpdateRequest(value.questionId, value.answer)
                is SingleIntAnswerResult ->
                    AnswerUpdateRequest(value.questionId, value.answer)
                is MultipleAnswerResult ->
                    AnswerUpdateRequest(value.questionId, value.answers)
                else -> null

            }
        }

    // build the request object for the api

    val request = SurveyUpdateRequest(answers)

    // upload data to task api

    val response = taskModule.updateSurvey(taskId, request)

    // convert the result to TaskHandleResult

    return response.fold({ TaskHandleResult.Error(it.message) }, { TaskHandleResult.Handled })

}