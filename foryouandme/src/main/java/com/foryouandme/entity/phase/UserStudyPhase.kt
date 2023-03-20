package com.foryouandme.entity.phase

import org.threeten.bp.ZonedDateTime

data class UserStudyPhase(
    val id: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime?,
    val phase: StudyPhase
) {

    companion object {

        fun mock(): UserStudyPhase =
            UserStudyPhase(
                id = "id",
                start = ZonedDateTime.now().minusDays(1),
                end = null,
                phase = StudyPhase.mock()
            )

    }

}
