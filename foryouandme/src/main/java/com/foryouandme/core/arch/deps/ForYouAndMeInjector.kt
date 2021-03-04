package com.foryouandme.core.arch.deps

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.foryouandme.core.arch.app.ForYouAndMeApp
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.arch.navigation.ForYouAndMeNavigationProvider
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.data.api.auth.AuthApi
import com.foryouandme.core.data.api.auth.response.UserResponse
import com.foryouandme.core.data.api.common.AnswerApi
import com.foryouandme.core.data.api.common.response.AnswerResponse
import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.core.data.api.common.response.QuestionResponse
import com.foryouandme.core.data.api.common.response.UnknownResourceResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityOptionResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedAlertResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedEducationalResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedRewardResponse
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.foryouandme.core.data.api.consent.informed.ConsentInfoApi
import com.foryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import com.foryouandme.core.data.api.consent.review.ConsentReviewApi
import com.foryouandme.core.data.api.consent.review.response.ConsentReviewResponse
import com.foryouandme.data.repository.consent.user.network.ConsentUserApi
import com.foryouandme.data.repository.consent.user.network.response.ConsentUserResponse
import com.foryouandme.core.data.api.feed.FeedApi
import com.foryouandme.core.data.api.feed.response.FeedResponse
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.core.data.api.integration.IntegrationApi
import com.foryouandme.core.data.api.integration.response.IntegrationResponse
import com.foryouandme.core.data.api.optins.OptInsApi
import com.foryouandme.core.data.api.optins.response.OptInsPermissionResponse
import com.foryouandme.core.data.api.optins.response.OptInsResponse
import com.foryouandme.core.data.api.screening.ScreeningApi
import com.foryouandme.core.data.api.screening.response.ScreeningResponse
import com.foryouandme.core.data.api.studyinfo.StudyInfoApi
import com.foryouandme.core.data.api.studyinfo.response.StudyInfoResponse
import com.foryouandme.core.data.api.survey.SurveyApi
import com.foryouandme.core.data.api.survey.response.SurveyAnswerResponse
import com.foryouandme.core.data.api.survey.response.SurveyBlockResponse
import com.foryouandme.core.data.api.survey.response.SurveyQuestionResponse
import com.foryouandme.core.data.api.survey.response.SurveyResponse
import com.foryouandme.data.repository.task.network.TaskApi
import com.foryouandme.data.repository.task.network.response.TaskResponse
import com.foryouandme.core.data.api.yourdata.YourDataApi
import com.foryouandme.core.researchkit.task.FYAMTaskConfiguration
import com.foryouandme.data.datasource.Environment
import com.foryouandme.researchkit.task.TaskConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import moe.banana.jsonapi2.ResourceAdapterFactory

class ForYouAndMeInjector(
    val app: ForYouAndMeApp,
    env: Environment,
    imageConfig: ImageConfiguration,
    videoConfig: VideoConfiguration,
    firebaseAnalytics: FirebaseAnalytics
) : Injector {

    /* --- navigator --- */

    override val navigator: Navigator =
        Navigator(ForYouAndMeNavigationProvider())

    /* --- cache --- */

    override val prefs: SharedPreferences by lazy {

        EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            app,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /* --- environment --- */

    override val environment: Environment = env

    /* --- image configuration --- */

    override val imageConfiguration: ImageConfiguration = imageConfig

    /* --- video configuration --- */

    override val videoConfiguration: VideoConfiguration = videoConfig

    /* --- analytics --- */
    override val firebaseAnalytics: FirebaseAnalytics = firebaseAnalytics

    /* --- moshi --- */

    override val moshi: Moshi =
        Moshi.Builder()
            .add(
                ResourceAdapterFactory.builder()
                    .add(UnknownResourceResponse::class.java)
                    .add(PageResponse::class.java)
                    .add(QuestionResponse::class.java)
                    .add(AnswerResponse::class.java)
                    .add(ScreeningResponse::class.java)
                    .add(ConsentInfoResponse::class.java)
                    .add(ConsentReviewResponse::class.java)
                    .add(ConsentUserResponse::class.java)
                    .add(OptInsPermissionResponse::class.java)
                    .add(OptInsResponse::class.java)
                    .add(IntegrationResponse::class.java)
                    .add(FeedResponse::class.java)
                    .add(QuickActivityResponse::class.java)
                    .add(QuickActivityOptionResponse::class.java)
                    .add(SurveyActivityResponse::class.java)
                    .add(TaskActivityResponse::class.java)
                    .add(UserResponse::class.java)
                    .add(StudyInfoResponse::class.java)
                    .add(FeedRewardResponse::class.java)
                    .build()
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    private val taskMoshi: Moshi =
        Moshi.Builder()
            .add(
                ResourceAdapterFactory.builder()
                    .add(UnknownResourceResponse::class.java)
                    .add(PageResponse::class.java)
                    .add(TaskResponse::class.java)
                    .add(QuickActivityResponse::class.java)
                    .add(QuickActivityOptionResponse::class.java)
                    .add(SurveyActivityResponse::class.java)
                    .add(TaskActivityResponse::class.java)
                    .build()
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    private val feedMoshi: Moshi =
        Moshi.Builder()
            .add(
                ResourceAdapterFactory.builder()
                    .add(UnknownResourceResponse::class.java)
                    .add(PageResponse::class.java)
                    .add(FeedResponse::class.java)
                    .add(QuickActivityResponse::class.java)
                    .add(QuickActivityOptionResponse::class.java)
                    .add(TaskActivityResponse::class.java)
                    .add(SurveyActivityResponse::class.java)
                    .add(FeedRewardResponse::class.java)
                    .add(FeedAlertResponse::class.java)
                    .add(FeedEducationalResponse::class.java)
                    .build()
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    private val surveyMoshi: Moshi =
        Moshi.Builder()
            .add(
                ResourceAdapterFactory.builder()
                    .add(UnknownResourceResponse::class.java)
                    .add(PageResponse::class.java)
                    .add(SurveyResponse::class.java)
                    .add(SurveyBlockResponse::class.java)
                    .add(SurveyQuestionResponse::class.java)
                    .add(SurveyAnswerResponse::class.java)
                    .build()
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    /* --- api --- */

    override val configurationApi: ConfigurationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val authApi: AuthApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val screeningApi: ScreeningApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentInfoApi: ConsentInfoApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentReviewApi: ConsentReviewApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentUserApi: ConsentUserApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val optInsApi: OptInsApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val integrationApi: IntegrationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val taskApi: TaskApi =
        getApiService(environment.getApiBaseUrl(), taskMoshi)

    override val answerApi: AnswerApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val feedApi: FeedApi =
        getApiService(environment.getApiBaseUrl(), feedMoshi)

    override val yourDataApi: YourDataApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val studyInfoApi: StudyInfoApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val surveyApi: SurveyApi =
        getApiService(environment.getApiBaseUrl(), surveyMoshi)

    /* --- task --- */

    override val taskConfiguration: TaskConfiguration =
        FYAMTaskConfiguration(
            environment,
            configurationModule(),
            imageConfiguration,
            moshi,
            taskModule(),
            surveyModule(),
            authModule(),
            errorModule(),
            analyticsModule()
        )

    /* --- modules --- */

    override fun permissionModule(): PermissionModule = PermissionModule(app, analyticsModule())

}