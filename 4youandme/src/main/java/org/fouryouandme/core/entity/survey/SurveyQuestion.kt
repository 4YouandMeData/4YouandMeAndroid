package org.fouryouandme.core.entity.survey

import org.threeten.bp.ZonedDateTime

sealed class SurveyQuestion {

    data class Date(
        val id: String,
        val text: String,
        val image: String?,
        val minDate: ZonedDateTime?,
        val maxDateTime: ZonedDateTime?
    ) : SurveyQuestion()

    data class Picker(
        val id: String,
        val text: String,
        val image: String?,
        val minValue: Int?,
        val maxValue: Int?,
        val minDisplayValue: String?,
        val maxDisplayValue: String?
    ) : SurveyQuestion()

}