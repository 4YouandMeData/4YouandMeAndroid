package com.foryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.Resource

open class ActivityDataResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "repeat_every") val repeatEvery: Int? = null,
    @field:Json(name = "card_color") val cardColor: String? = null,
    @field:Json(name = "task_action_button_label") val button: String? = null,
) : Resource()