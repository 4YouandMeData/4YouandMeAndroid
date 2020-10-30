package com.fouryouandme.core.entity.notifiable

import android.graphics.Bitmap
import com.fouryouandme.core.entity.configuration.HEXGradient

sealed class StudyNotifiable

data class FeedReward(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedAlert(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedEducational(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val linkUrl: String?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()