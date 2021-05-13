package com.foryouandme.researchkit.skip

sealed class SurveySkip {

    data class Range(val min: Int?, val max: Int?, val target: SkipTarget) : SurveySkip()

    data class Answer(val answerId: String, val target: SkipTarget) : SurveySkip()

}

