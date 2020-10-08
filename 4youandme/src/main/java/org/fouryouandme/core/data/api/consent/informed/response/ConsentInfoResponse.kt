package org.fouryouandme.core.data.api.consent.informed.response

import arrow.core.Either
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.data.api.common.response.QuestionResponse
import org.fouryouandme.core.entity.consent.informed.ConsentInfo

@JsonApi(type = "informed_consent")
data class ConsentInfoResponse(
    @field:Json(name = "minimum_required_correct_answers")
    val minimumAnswer: Int? = null,
    @field:Json(name = "questions")
    val questions: HasMany<QuestionResponse>? = null,
    @field:Json(name = "pages")
    val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page")
    val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page")
    val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page")
    val failurePage: HasOne<PageResponse>? = null
) : Resource() {

    suspend fun toConsentInfo(document: ObjectDocument<ConsentInfoResponse>): ConsentInfo? =
        Either.catch {

            ConsentInfo(
                minimumAnswer!!,
                questions?.get(document)
                    ?.mapNotNull { it.toConsentQuestion(document) }!!,
                pages?.get(document)
                    ?.mapNotNull { it.toPage(document) }!!,
                welcomePage?.get(document)?.toPage(document)!!,
                successPage?.get(document)?.toPage(document)!!,
                failurePage?.get(document)?.toPage(document)!!
            )

        }.orNull()
}

