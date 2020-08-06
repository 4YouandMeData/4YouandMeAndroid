package org.fouryouandme.core.data.api.common.response.activity

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import org.fouryouandme.core.entity.activity.QuickActivity
import org.fouryouandme.core.entity.activity.QuickActivityAnswer
import org.fouryouandme.core.entity.configuration.HEXGradient

@JsonApi(type = "quick_activity")
class QuickActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    startColor: String? = null,
    endColor: String? = null,
    button: String? = null,
    @field:Json(name = "quick_activity_options")
    val options: HasMany<QuickActivityOptionResponse>? = null
) : ActivityDataResponse(title, description, repeatEvery, startColor, endColor, button) {

    fun toQuickActivity(): QuickActivity =
        QuickActivity(
            id,
            title.toOption(),
            description.toOption(),
            button.toOption(),
            Option.fx { HEXGradient(!startColor.toOption(), !endColor.toOption()) },
            getAnswer(1),
            getAnswer(2),
            getAnswer(3),
            getAnswer(4),
            getAnswer(5),
            getAnswer(6)
        )


    private fun getAnswer(position: Int): Option<QuickActivityAnswer> =
        options?.get(document)
            ?.firstOrNull { it.position == position }
            .toOption()
            .map { it.toQuickActivityAnswer() }

}