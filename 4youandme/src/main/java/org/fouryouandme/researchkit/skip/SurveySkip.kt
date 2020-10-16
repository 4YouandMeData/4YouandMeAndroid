package org.fouryouandme.researchkit.skip

sealed class SurveySkip {

    data class Range(val min: Int?, val max: Int?, val stepId: String) : SurveySkip()

    data class Answer(val answerId: String, val stepId: String) : SurveySkip()

}