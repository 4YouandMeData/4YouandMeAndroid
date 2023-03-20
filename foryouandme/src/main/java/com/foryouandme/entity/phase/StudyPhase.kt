package com.foryouandme.entity.phase

import com.foryouandme.entity.mock.Mock

data class StudyPhase(
    val id: String,
    val name: String,
) {

    companion object {

        fun mock(): StudyPhase =
            StudyPhase(id = "id", name = Mock.name)

    }

}