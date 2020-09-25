package org.fouryouandme.core.entity.activity

import android.graphics.Bitmap
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.researchkit.task.TaskIdentifiers

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
    val id: String,
    val title: String?,
    val description: String?,
    val button: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val activityType: TaskActivityType?
) : StudyActivity()

sealed class TaskActivityType(val typeId: String, val identifier: String) {

    object VideoDiary : TaskActivityType("video_diary", TaskIdentifiers.VIDEO_DIARY)
    object GaitTask : TaskActivityType("gait_task", TaskIdentifiers.GAIT)
    object WalkTask : TaskActivityType("walk_task", TaskIdentifiers.FITNESS)


    companion object {

        fun fromType(type: String): TaskActivityType? =
            when (type) {
                VideoDiary.typeId -> VideoDiary
                GaitTask.typeId -> GaitTask
                WalkTask.typeId -> WalkTask
                else -> null
            }

    }

}