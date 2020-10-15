package org.fouryouandme.core.entity.survey

import org.fouryouandme.core.entity.page.Page

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
    val introPage: Page,
    val successPage: Page?,
    val questions: List<SurveyQuestion>,
)
