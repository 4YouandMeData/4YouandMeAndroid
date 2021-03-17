package com.foryouandme.core.data.api.common.response.meta

import com.squareup.moshi.Json

data class MetaResponse(
    @Json(name = "resolved_templates") val resolvedTemplates: List<ResolvedTemplateResponse>? = null
) {

    fun applyMeta(source: String): String {

        var replacedSource = source

        resolvedTemplates?.forEach {

            if (it.resolved != null && it.variable != null)
                replacedSource =
                    replacedSource.replace("{{${it.variable}}}", it.resolved)

        }

        return replacedSource

    }

}

data class ResolvedTemplateResponse(
    @Json(name = "variable") val variable: String? = null,
    @Json(name = "resolved") val resolved: String? = null
)