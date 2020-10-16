package org.fouryouandme.core.entity.survey

sealed class SurveyTarget {

    data class Range(val min: Int?, val max: Int?, val questionId: String) : SurveyTarget()

    data class Answer(val answerId: String, val questionId: String) : SurveyTarget()

}