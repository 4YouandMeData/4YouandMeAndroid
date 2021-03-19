package com.foryouandme.data.repository.survey.network.response

import com.foryouandme.core.ext.emptyOrBlankToNull
import com.foryouandme.entity.survey.SurveyQuestion
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.roundToInt

@JsonApi(type = "question")
data class SurveyQuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "question_type") val questionType: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "min_date") val minDate: String? = null,
    @field:Json(name = "max_date") val maxDate: String? = null,
    @field:Json(name = "min") val minValue: Int? = null,
    @field:Json(name = "max") val maxValue: Int? = null,
    @field:Json(name = "min_display") val minDisplay: String? = null,
    @field:Json(name = "max_display") val maxDisplay: String? = null,
    @field:Json(name = "possible_answers") val answers: HasMany<SurveyAnswerResponse>? = null,
    @field:Json(name = "placeholder") val placeholder: String? = null,
    @field:Json(name = "max_characters") val maxCharacters: Int? = null,
    @field:Json(name = "interval") val interval: String? = null,
    @field:Json(name = "targets") val targets: List<SurveyQuestionTargetResponse>? = null
) : Resource() {

    fun toSurveyQuestion(): SurveyQuestion? {
        return when (questionType) {

            "SurveyQuestionDate" -> {

                val minDate =
                    minDate?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
                val maxDate =
                    maxDate?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }

                if ( // if max and min are valid check if min < max
                    minDate != null &&
                    maxDate != null &&
                    (minDate.isAfter(maxDate) || minDate.isEqual(maxDate))
                ) null
                else
                    when (null) {
                        text -> null
                        else ->
                            SurveyQuestion.Date(
                                id,
                                text,
                                image,
                                minDate,
                                maxDate,
                            )
                    }
            }
            "SurveyQuestionNumerical" -> {

                when (null) {
                    text, minValue, maxValue -> null
                    else -> {
                        if (minValue >= maxValue) null
                        else
                            SurveyQuestion.Numerical(
                                id,
                                text,
                                image,
                                minValue,
                                maxValue,
                                minDisplay.emptyOrBlankToNull(),
                                maxDisplay.emptyOrBlankToNull(),
                                targets?.mapNotNull { it.toRange() } ?: emptyList()
                            )
                    }
                }

            }
            "SurveyQuestionPickOne" -> {

                val surveyAnswers =
                    answers?.get(document)?.mapNotNull { it.toSurveyAnswer() }

                when (null) {
                    surveyAnswers, text -> null
                    else -> {
                        if (surveyAnswers.isEmpty()) null
                        else
                            SurveyQuestion.PickOne(
                                id,
                                text,
                                image,
                                surveyAnswers,
                                targets?.mapNotNull { it.toAnswer() } ?: emptyList()
                            )
                    }
                }
            }
            "SurveyQuestionPickMany" -> {

                val surveyAnswers =
                    answers?.get(document)?.mapNotNull { it.toSurveyAnswer() }

                when (null) {
                    surveyAnswers, text -> null
                    else -> {
                        if (surveyAnswers.isEmpty()) null
                        else
                            SurveyQuestion.PickMany(
                                id,
                                text,
                                image,
                                surveyAnswers,
                                targets?.mapNotNull { it.toAnswer() } ?: emptyList(),
                            )
                    }
                }

            }
            "SurveyQuestionText" ->

                when (null) {
                    text -> null
                    else -> {
                        if (maxCharacters?.let { it <= 0 } == true) null
                        else
                            SurveyQuestion.TextInput(
                                id,
                                text,
                                image,
                                placeholder,
                                maxCharacters
                            )
                    }
                }

            "SurveyQuestionScale" -> {

                val scaleInterval = interval?.toFloatOrNull()?.roundToInt()

                val isValidInterval =
                    if (minValue != null && maxValue != null)
                        scaleInterval?.let { it >= 0 && (minValue + it) <= maxValue } ?: true
                    else null

                when (null) {
                    minValue, maxValue, isValidInterval, text -> null
                    else -> {
                        if (minValue >= maxValue || isValidInterval.not()) null
                        else
                            SurveyQuestion.Scale(
                                id,
                                text,
                                image,
                                minValue,
                                maxValue,
                                scaleInterval,
                                targets?.mapNotNull { it.toRange() } ?: emptyList()
                            )
                    }
                }

            }
            "SurveyQuestionRange" -> {

                when (null) {
                    minValue, maxValue, text -> null
                    else -> {
                        if (minValue >= maxValue) null
                        else
                            SurveyQuestion.Range(
                                id,
                                text,
                                image,
                                minValue,
                                maxValue,
                                minDisplay,
                                maxDisplay,
                                targets?.mapNotNull { it.toRange() } ?: emptyList()
                            )
                    }
                }

            }
            else -> null

        }
    }

}