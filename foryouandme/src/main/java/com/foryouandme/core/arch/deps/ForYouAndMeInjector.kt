package com.foryouandme.core.arch.deps

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.foryouandme.core.arch.app.ForYouAndMeApp
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.arch.navigation.ForYouAndMeNavigationProvider
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.data.api.optins.OptInsApi
import com.foryouandme.core.data.api.optins.response.OptInsPermissionResponse
import com.foryouandme.core.data.api.optins.response.OptInsResponse
import com.foryouandme.core.data.api.yourdata.YourDataApi
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.answer.network.AuthAnswerApi
import com.foryouandme.data.repository.auth.answer.network.response.AnswerResponse
import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.data.repository.auth.answer.network.response.QuestionResponse
import com.foryouandme.data.repository.auth.answer.network.response.UnknownResourceResponse
import com.foryouandme.data.repository.auth.answer.network.response.activity.QuickActivityOptionResponse
import com.foryouandme.data.repository.auth.answer.network.response.activity.QuickActivityResponse
import com.foryouandme.data.repository.auth.answer.network.response.activity.SurveyActivityResponse
import com.foryouandme.data.repository.auth.answer.network.response.activity.TaskActivityResponse
import com.foryouandme.data.repository.auth.answer.network.response.notifiable.FeedAlertResponse
import com.foryouandme.data.repository.auth.answer.network.response.notifiable.FeedEducationalResponse
import com.foryouandme.data.repository.auth.answer.network.response.notifiable.FeedRewardResponse
import com.foryouandme.data.repository.auth.integration.network.IntegrationApi
import com.foryouandme.data.repository.auth.integration.network.response.IntegrationResponse
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.data.repository.auth.screening.network.ScreeningApi
import com.foryouandme.data.repository.auth.screening.network.response.ScreeningResponse
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.foryouandme.core.data.api.consent.informed.ConsentInfoApi
import com.foryouandme.data.repository.auth.consent.network.response.ConsentInfoResponse
import com.foryouandme.data.repository.auth.consent.network.response.ConsentReviewResponse
import com.foryouandme.data.repository.consent.user.network.ConsentUserApi
import com.foryouandme.data.repository.consent.user.network.response.ConsentUserResponse
import com.foryouandme.data.repository.feed.network.FeedApi
import com.foryouandme.data.repository.feed.network.response.FeedResponse
import com.foryouandme.data.repository.study.network.StudyInfoApi
import com.foryouandme.data.repository.study.network.response.StudyInfoResponse
import com.foryouandme.data.repository.survey.network.SurveyApi
import com.foryouandme.data.repository.survey.network.response.SurveyAnswerResponse
import com.foryouandme.data.repository.survey.network.response.SurveyBlockResponse
import com.foryouandme.data.repository.survey.network.response.SurveyQuestionResponse
import com.foryouandme.data.repository.survey.network.response.SurveyResponse
import com.foryouandme.data.repository.task.network.TaskApi
import com.foryouandme.data.repository.task.network.response.TaskResponse
import com.foryouandme.data.repository.user.network.UserResponse
import com.foryouandme.data.repository.usersettings.network.UserSettingsApi
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

    override val answerApi: AuthAnswerApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val feedApi: FeedApi =
        getApiService(environment.getApiBaseUrl(), feedMoshi)

    override val yourDataApi: YourDataApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val studyInfoApi: StudyInfoApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val surveyApi: SurveyApi =
        getApiService(environment.getApiBaseUrl(), surveyMoshi)

    override val userSettingsApi: UserSettingsApi =
        getApiService(environment.getApiBaseUrl(), moshi)


    /* --- modules --- */

    override fun permissionModule(): PermissionModule = PermissionModule(app, analyticsModule())
}