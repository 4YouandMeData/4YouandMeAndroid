package com.foryouandme.entity.phase

import com.foryouandme.entity.mock.Mock
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
                start = Mock.zoneDateTime,
                end = null,
                phase = StudyPhase.mock()
            )

    }

}
