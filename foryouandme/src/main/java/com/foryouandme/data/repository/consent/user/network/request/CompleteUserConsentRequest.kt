package com.foryouandme.data.repository.consent.user.network.request

import com.squareup.moshi.Json
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

data class CompleteUserConsentRequest(
    @Json(name = "on_boarding_completed_at") val onBoardingCompletedAt: String,
    @Json(name = "agree") val agree: Boolean
) {

    companion object {

        fun build(): CompleteUserConsentRequest =
            CompleteUserConsentRequest(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                true
            )

    }

}