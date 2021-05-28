package com.foryouandme.entity.notifiable

import android.graphics.Bitmap
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.mock.Mock

sealed class StudyNotifiable

data class FeedReward(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable() {

    companion object {

        fun mock(): FeedReward =
            FeedReward(
                id ="id",
                title = Mock.title,
                description = Mock.body,
                gradient =
                HEXGradient(
                    Configuration.mock().theme.primaryColorStart.hex,
                    Configuration.mock().theme.primaryColorEnd.hex
                ),
                image = null,
                action = FeedAction.Faq,
                taskActionButtonLabel = Mock.button
            )

    }

}

data class FeedAlert(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable() {

    companion object {

        fun mock(): FeedAlert =
            FeedAlert(
                id ="id",
                title = Mock.title,
                description = Mock.body,
                gradient =
                HEXGradient(
                    Configuration.mock().theme.primaryColorStart.hex,
                    Configuration.mock().theme.primaryColorEnd.hex
                ),
                image = null,
                action = FeedAction.Faq,
                taskActionButtonLabel = Mock.button
            )

    }

}

data class FeedEducational(
    val id: String,
    val title: String?,
    val description: String?,
    val gradient: HEXGradient?,
    val image: Bitmap?,
    val action: FeedAction?,
    val taskActionButtonLabel: String?
) : StudyNotifiable() {

    companion object {

        fun mock(): FeedEducational =
            FeedEducational(
                id ="id",
                title = Mock.title,
                description = Mock.body,
                gradient =
                HEXGradient(
                    Configuration.mock().theme.primaryColorStart.hex,
                    Configuration.mock().theme.primaryColorEnd.hex
                ),
                image = null,
                action = FeedAction.Faq,
                taskActionButtonLabel = Mock.button
            )

    }

}


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