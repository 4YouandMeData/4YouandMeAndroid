package org.fouryouandme.core.entity.activity

import arrow.core.None
import arrow.core.Option
import arrow.core.some
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.researchkit.task.ETaskType

sealed class StudyActivity

data class QuickActivity(
    val id: String,
    val title: Option<String>,
    val description: Option<String>,
    val button: Option<String>,
    val gradient: Option<HEXGradient>,
    val answer1: Option<QuickActivityAnswer>,
    val answer2: Option<QuickActivityAnswer>,
    val answer3: Option<QuickActivityAnswer>,
    val answer4: Option<QuickActivityAnswer>,
    val answer5: Option<QuickActivityAnswer>,
    val answer6: Option<QuickActivityAnswer>
) : StudyActivity()

data class QuickActivityAnswer(
    val id: String,
    val text: Option<String>,
    val image: Option<String>,
    val selectedImage: Option<String>
)

data class TaskActivity(
    val id: String,
    val title: Option<String>,
    val description: Option<String>,
    val button: Option<String>,
    val gradient: Option<HEXGradient>,
    val image: Option<String>,
    val activityType: Option<TaskActivityType>
) : StudyActivity()

sealed class TaskActivityType(val typeId: String, val type: ETaskType) {

    object VideoDiary : TaskActivityType("video_diary", ETaskType.VIDEO_DIARY)
    object GaitTask : TaskActivityType("gait_task", ETaskType.GAIT)
    object WalkTask : TaskActivityType("walk_task", ETaskType.WALK)


    companion object {

        fun fromType(type: String): Option<TaskActivityType> =
            when (type) {
                VideoDiary.typeId -> VideoDiary.some()
                GaitTask.typeId -> GaitTask.some()
                WalkTask.typeId -> WalkTask.some()
                else -> None
            }

    }

}