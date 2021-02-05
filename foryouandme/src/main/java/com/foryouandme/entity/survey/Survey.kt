package com.foryouandme.entity.survey

import com.foryouandme.entity.page.Page

data class Survey(
    val id: String,
    val title: String?,
    val description: String?,
    val active: Boolean? = null,
    val color: String? = null,
    val image: String? = null,
    val surveyBlocks: List<SurveyBlock>
)

data class SurveyBlock(
    val id: String,
    val pages: List<Page>,
    val introPage: Page?,
    val successPage: Page?,
    val questions: List<SurveyQuestion>,
)
