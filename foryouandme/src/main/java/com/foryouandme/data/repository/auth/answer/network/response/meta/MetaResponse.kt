package com.foryouandme.data.repository.auth.answer.network.response.meta

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

        resolvedTemplates?.forEach { response ->

            if (response.resolved != null && response.variable != null) {
                val date = getDate(response.resolved)
                replacedSource = if (date != null) {
                    val formattedDate =
                        date.format(DateTimeFormatter.ofPattern("MMM dd"))
                            .replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                else it.toString()
                            }
                    replacedSource.replace("{{${response.variable}}}", formattedDate)
                } else
                    replacedSource.replace("{{${response.variable}}}", response.resolved)
            }

        }

        return replacedSource

    }

    private fun getDate(dateStr: String): LocalDate? =
        catchToNull {
            LocalDate.parse(
                dateStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            )
        }
}

data class ResolvedTemplateResponse(
    @Json(name = "variable") val variable: String? = null,
    @Json(name = "resolved") val resolved: String? = null
)