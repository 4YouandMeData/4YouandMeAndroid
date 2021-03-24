package com.foryouandme.core.data.api.common.response.meta

import com.foryouandme.core.ext.catchToNull
import com.squareup.moshi.Json
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

data class MetaResponse(
        @Json(name = "resolved_templates") val resolvedTemplates: List<ResolvedTemplateResponse>? = null
) {

    fun applyMeta(source: String): String {

        var replacedSource = source

        resolvedTemplates?.forEach {

            if (it.resolved != null && it.variable != null) {
                val date = getDate(it.resolved)
                replacedSource = if (date != null) {
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("MMM dd")).capitalize(Locale.getDefault())
                    replacedSource.replace("{{${it.variable}}}", formattedDate)
                } else
                    replacedSource.replace("{{${it.variable}}}", it.resolved)
            }

        }

        return replacedSource

    }

    private fun getDate(dateStr: String): LocalDate? =
            catchToNull {
                LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            }
}

data class ResolvedTemplateResponse(
        @Json(name = "variable") val variable: String? = null,
        @Json(name = "resolved") val resolved: String? = null
)