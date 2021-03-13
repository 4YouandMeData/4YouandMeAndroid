package com.foryouandme.entity.activity

import android.graphics.Bitmap
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.page.Page
import com.foryouandme.researchkit.task.TaskIdentifiers

sealed class StudyActivity

data class QuickActivity(
    val id: String,
    val title: String?,
    val description: String?,
    val button: String?,
    val gradient: HEXGradient?,
    val answer1: QuickActivityAnswer?,
    val answer2: QuickActivityAnswer?,
    val answer3: QuickActivityAnswer?,
    val answer4: QuickActivityAnswer?,
    val answer5: QuickActivityAnswer?,
    val answer6: QuickActivityAnswer?
) : StudyActivity()

data class QuickActivityAnswer(
    val id: String,
    val text: String?,
    val image: Bitmap?,
    val selectedImage: Bitmap?
)

data class TaskActivity(
    val taskId: String,
    val activityId: String,
    val title: String?,
    val description: String?,
    val button: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val activityType: TaskActivityType?,
    val welcomePage: Page,
    val pages: List<Page>,
    val successPage: Page?,
    val reschedule: Reschedule?
) : StudyActivity()

sealed class TaskActivityType(val typeId: String, val type: String) {

    object VideoDiary : TaskActivityType("video_diary", TaskIdentifiers.VIDEO_DIARY)
    object GaitTask : TaskActivityType("gait_task", TaskIdentifiers.GAIT)
    object WalkTask : TaskActivityType("walk_task", TaskIdentifiers.FITNESS)
    object TrailMaking : TaskActivityType("trail_making_task", TaskIdentifiers.TRAIL_MAKING)
    object ReactionTime : TaskActivityType("reaction_time_task", TaskIdentifiers.REACTION_TIME)
    object CamCogPvt : TaskActivityType("camcog_pvt", TaskIdentifiers.CAMCOG)
    object CamCogNbx : TaskActivityType("camcog_nbx", TaskIdentifiers.CAMCOG)
    object CamCogEbt : TaskActivityType("camcog_ebt", TaskIdentifiers.CAMCOG)
    object Survey : TaskActivityType("survey", TaskIdentifiers.SURVEY)

    companion object {

        fun fromType(type: String): TaskActivityType? =
            when (type) {
                VideoDiary.typeId -> VideoDiary
                GaitTask.typeId -> GaitTask
                WalkTask.typeId -> WalkTask
                CamCogPvt.typeId -> CamCogPvt
                CamCogEbt.typeId -> CamCogEbt
                CamCogNbx.typeId -> CamCogNbx
                Survey.typeId -> Survey
                else -> null
            }

    }

}

data class Reschedule(
    val rescheduleIn: Int,
    val rescheduleTimes: Int,
    val rescheduledTimes: Int?
) {

    companion object {

        fun Reschedule?.isEnabled(): Boolean =
            this != null && rescheduleTimes > 0 && rescheduledTimes ?: 0 < rescheduleTimes

    }

}

