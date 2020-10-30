package com.fouryouandme.core.data.api.task.request

import com.squareup.moshi.Json

data class QuickActivityUpdateRequest(
    @Json(name = "selected_quick_activity_option_id") val selectedQuickActivityOptionId: Int
)