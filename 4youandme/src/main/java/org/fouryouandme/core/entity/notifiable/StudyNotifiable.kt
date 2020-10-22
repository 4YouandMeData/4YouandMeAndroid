package org.fouryouandme.core.entity.notifiable

import android.graphics.Bitmap

sealed class StudyNotifiable

data class FeedReward(
    val id: String,
    val title: String?,
    val description: String?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedAlert(
    val id: String,
    val title: String?,
    val description: String?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedEducational(
    val id: String,
    val title: String?,
    val description: String?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()