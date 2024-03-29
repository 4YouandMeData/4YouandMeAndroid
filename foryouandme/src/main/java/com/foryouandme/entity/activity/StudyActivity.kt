package com.foryouandme.entity.activity

import android.graphics.Bitmap
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.mock.Mock
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
) : StudyActivity() {

    companion object {

        fun mock(): QuickActivity =
            QuickActivity(
                id = "id",
                title = Mock.title,
                description = Mock.body,
                button = Mock.button,
                gradient =
                HEXGradient(
                    Configuration.mock().theme.primaryColorStart.hex,
                    Configuration.mock().theme.primaryColorEnd.hex
                ),
                answer1 = QuickActivityAnswer.mock(),
                answer2 = QuickActivityAnswer.mock(),
                answer3 = QuickActivityAnswer.mock(),
                answer4 = QuickActivityAnswer.mock(),
                answer5 = QuickActivityAnswer.mock(),
                answer6 = QuickActivityAnswer.mock()
            )

    }

}

data class QuickActivityAnswer(
    val id: String,
    val text: String?,
    val image: String?,
    val selectedImage: String?
) {

    companion object {

        fun mock(): QuickActivityAnswer =
            QuickActivityAnswer(
                id = "id",
                text = Mock.name,
                image = null,
                selectedImage = null
            )

    }

}

data class TaskActivity(
    val taskId: String,
    val activityId: String,
    val title: String?,
    val description: String?,
    val button: String?,
    val gradient: HEXGradient?,
    val image: String?,
    val activityType: TaskActivityType?,
    val welcomePage: Page,
    val pages: List<Page>,
    val successPage: Page?,
    val reschedule: Reschedule?
) : StudyActivity() {

    companion object {

        fun mock(): TaskActivity =
            TaskActivity(
                taskId = "task_id",
                activityId = "activity_id",
                title = Mock.title,
                description = Mock.body,
                button = Mock.button,
                gradient =
                HEXGradient(
                    Configuration.mock().theme.primaryColorStart.hex,
                    Configuration.mock().theme.primaryColorEnd.hex
                ),
                image = null,
                activityType = TaskActivityType.Survey,
                welcomePage = Page.mock(),
                pages = listOf(Page.mock(), Page.mock()),
                successPage = Page.mock(),
                reschedule = null
            )

    }

}

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
    object HolePeg : TaskActivityType("nine_hole", TaskIdentifiers.HOLE_PEG)

    companion object {

        fun fromType(type: String): TaskActivityType? =
            when (type) {
                VideoDiary.typeId -> VideoDiary
                GaitTask.typeId -> GaitTask
                WalkTask.typeId -> WalkTask
                TrailMaking.typeId -> TrailMaking
                ReactionTime.typeId -> ReactionTime
                CamCogPvt.typeId -> CamCogPvt
                CamCogEbt.typeId -> CamCogEbt
                CamCogNbx.typeId -> CamCogNbx
                Survey.typeId -> Survey
                HolePeg.typeId -> HolePeg
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

