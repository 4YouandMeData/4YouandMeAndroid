package com.foryouandme.core.entity.notifiable

import android.graphics.Bitmap
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.integration.IntegrationApp

sealed class StudyNotifiable

data class FeedReward(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedAlert(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()

data class FeedEducational(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable()


sealed class FeedAction {

    object Feed : FeedAction()
    object Tasks : FeedAction()
    object YourData : FeedAction()
    object StudyInfo : FeedAction()
    object AboutYou : FeedAction()
    object Faq : FeedAction()
    object Rewards : FeedAction()
    object Contacts : FeedAction()
    data class Integration(val app: IntegrationApp) : FeedAction()
    data class Web(val url: String) : FeedAction()

}