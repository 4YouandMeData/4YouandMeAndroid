package org.fouryouandme.core.entity.notifiable

import android.graphics.Bitmap

sealed class StudyNotifiable

data class FeedReward(
    val id: String,
    val title: String?,
    val description: String?,
    val image: Bitmap?
) : StudyNotifiable()