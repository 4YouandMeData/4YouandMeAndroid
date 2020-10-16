package org.fouryouandme.core.data.api.survey.response

import arrow.core.Either
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.survey.SurveyQuestion
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset

@JsonApi(type = "question")
data class SurveyQuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "type") val questionType: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "min_date") val minDate: String? = null,
    @field:Json(name = "max_date") val maxDate: String? = null,
    @field:Json(name = "min") val minValue: Int? = null,
    @field:Json(name = "max") val maxValue: Int? = null,
    @field:Json(name = "min_display") val minDisplay: String? = null,
    @field:Json(name = "max_display") val maxDisplay: String? = null,
    //@field:Json(name = "possible_answers") val possibleAnswers: List<> TODO: gestire la lista delle possibili risposte
    @field:Json(name = "placeholder") val placeholder: String? = null,
    @field:Json(name = "max_characters") val maxCharacters: Int? = null,
    @field:Json(name = "interval") val interval: String? = null
) : Resource() {

    suspend fun toSurveyQuestion(): SurveyQuestion? =
        Either.catch {
            when (questionType) {

                "SurveyQuestionDate" ->
                    SurveyQuestion.Date(
                        id,
                        text!!,
                        image,
                        Instant.parse(minDate).atZone(ZoneOffset.UTC),
                        Instant.parse(maxDate).atZone(ZoneOffset.UTC),
                    )

                "SurveyQuestionNumerical" ->
                    SurveyQuestion.Numerical(
                        id,
                        text!!,
                        image,
                        minValue,
                        maxValue,
                        minDisplay,
                        maxDisplay
                    )

                "SurveyQuestionPickOne" ->
                    SurveyQuestion.PickOne(
                        id,
                        text!!,
                        image,
                        // TODO: aggiungere lista possibili risposte
                    )

                "SurveyQuestionPickMany" ->
                    SurveyQuestion.PickMany(
                        id,
                        text!!,
                        image,
                        // TODO: aggiungere lista possibili risposte
                    )

                "SurveyQuestionText" ->
                    SurveyQuestion.TextInput(
                        id,
                        text!!,
                        image,
                        placeholder,
                        maxCharacters
                    )

                "SurveyQuestionScale" ->
                    SurveyQuestion.Scale(
                        id,
                        text!!,
                        image,
                        minValue,
                        maxValue,
                        interval
                    )

                "SurveyQuestionRange" ->
                    SurveyQuestion.Range(
                        id,
                        text!!,
                        image,
                        minValue,
                        maxValue,
                        minDisplay,
                        maxDisplay
                    )
                else -> null

            }
        }.orNull()

}