package org.fouryouandme.core.arch.deps

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.modules.*
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.data.api.auth.AuthApi
import org.fouryouandme.core.data.api.common.AnswerApi
import org.fouryouandme.core.data.api.configuration.ConfigurationApi
import org.fouryouandme.core.data.api.consent.informed.ConsentInfoApi
import org.fouryouandme.core.data.api.consent.review.ConsentReviewApi
import org.fouryouandme.core.data.api.consent.user.ConsentUserApi
import org.fouryouandme.core.data.api.feed.FeedApi
import org.fouryouandme.core.data.api.integration.IntegrationApi
import org.fouryouandme.core.data.api.optins.OptInsApi
import org.fouryouandme.core.data.api.screening.ScreeningApi
import org.fouryouandme.core.data.api.task.TaskApi

interface Injector {

    /* --- runtime --- */

    val runtimeContext: RuntimeContext

    /* --- navigation --- */

    val navigator: Navigator

    /* --- cache --- */

    val prefs: SharedPreferences

    /* --- environment --- */

    val environment: Environment

    /* --- image configuration --- */

    val imageConfiguration: ImageConfiguration

    /* --- json --- */

    val moshi: Moshi

    /* --- api --- */

    val configurationApi: ConfigurationApi
    val authApi: AuthApi
    val screeningApi: ScreeningApi
    val consentInfoApi: ConsentInfoApi
    val consentReviewApi: ConsentReviewApi
    val consentUserApi: ConsentUserApi
    val optInsApi: OptInsApi
    val integrationApi: IntegrationApi
    val taskApi: TaskApi
    val answerApi: AnswerApi
    val feedApi: FeedApi

    /* --- modules --- */

    fun errorModule(): ErrorModule = ErrorModule(moshi)

    fun configurationModule(): ConfigurationModule =
        ConfigurationModule(
            configurationApi,
            prefs,
            moshi,
            environment,
            errorModule()
        )

    fun authModule(): AuthModule =
        AuthModule(
            authApi,
            prefs,
            moshi,
            environment,
            errorModule()
        )

    fun taskModule(): TaskModule =
        TaskModule(
            taskApi,
            moshi,
            environment,
            errorModule(),
            authModule()
        )

    fun consentReviewModule(): ConsentReviewModule =
        ConsentReviewModule(
            consentReviewApi,
            errorModule(),
            authModule(),
            environment
        )

    fun feedModule(): FeedModule =
        FeedModule(
            feedApi,
            moshi,
            environment,
            errorModule(),
            authModule()
        )
}