package org.fouryouandme.core.entity.survey

import org.threeten.bp.LocalDate

sealed class SurveyQuestion {

    data class Date(
        val id: String,
        val text: String,
        val image: String?,
        val minDate: LocalDate?,
        val maxDate: LocalDate?
    ) : SurveyQuestion()

    data class Numerical(
        val id: String,
        val text: String,
        val image: String?,
        val minValue: Int,
        val maxValue: Int,
        val minDisplayValue: String?,
        val maxDisplayValue: String?
    ) : SurveyQuestion()

    data class PickOne(
        val id: String,
        val text: String,
        val image: String?,
        val answers: List<SurveyAnswer>
    ) : SurveyQuestion()

    data class PickMany(
        val id: String,
        val text: String,
        val image: String?,
        val answers: List<SurveyAnswer>
    ) : SurveyQuestion()

    data class TextInput(
        val id: String,
        val text: String,
        val image: String?,
        val placeholder: String?,
        val maxCharacters: Int?
    ) : SurveyQuestion()

    data class Scale(
        val id: String,
        val text: String,
        val image: String?,
        val min: Int,
        val max: Int,
        val interval: Int?
    ) : SurveyQuestion()

    data class Range(
        val id: String,
        val text: String,
        val image: String?,
        val min: Int,
        val max: Int,
        val minDisplay: String?,
        val maxDisplay: String?
    ) : SurveyQuestion()
}