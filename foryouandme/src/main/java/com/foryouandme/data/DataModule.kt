package com.foryouandme.data

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.foryouandme.data.repository.user.network.UserResponse
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
import com.foryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import com.foryouandme.core.data.api.consent.review.response.ConsentReviewResponse
import com.foryouandme.core.data.api.integration.response.IntegrationResponse
import com.foryouandme.core.data.api.optins.response.OptInsPermissionResponse
import com.foryouandme.core.data.api.optins.response.OptInsResponse
import com.foryouandme.data.repository.auth.screening.network.response.ScreeningResponse
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.datasource.network.SerializeNulls
import com.foryouandme.data.repository.consent.user.network.response.ConsentUserResponse
import com.foryouandme.data.repository.feed.network.response.FeedResponse
import com.foryouandme.data.repository.study.network.response.StudyInfoResponse
import com.foryouandme.data.repository.survey.network.response.SurveyAnswerResponse
import com.foryouandme.data.repository.survey.network.response.SurveyBlockResponse
import com.foryouandme.data.repository.survey.network.response.SurveyQuestionResponse
import com.foryouandme.data.repository.survey.network.response.SurveyResponse
import com.foryouandme.data.repository.task.network.response.TaskResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import moe.banana.jsonapi2.ResourceAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
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
            .add(SerializeNulls.JSON_ADAPTER_FACTORY)
            .build()

    @Singleton
    @Provides
    @Named(TASK_MOSHI)
    fun provideTaskMoshi(): Moshi =
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

    @Singleton
    @Provides
    @Named(FEED_MOSHI)
    fun provideFeedMoshi(): Moshi =
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

    @Singleton
    @Provides
    @Named(SURVEY_MOSHI)
    fun provideSurveyMoshi(): Moshi =
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

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {

        val masterKey =
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()


        return EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ForYouAndMeDatabase =
        Room.databaseBuilder(
            context,
            ForYouAndMeDatabase::class.java,
            "for_you_and_me_database"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideLocationRequest(): LocationRequest =
        LocationRequest()
            .apply {

                // Sets the desired interval for active location updates. This interval is inexact.
                // You may not receive updates at all if no location sources are available,
                // or you may receive them less frequently than requested. You may also receive
                // updates more frequently than requested if other applications are requesting
                // location at a more frequent interval.
                //
                // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
                // targetSdkVersion) may receive updates less frequently than this interval when
                // the app is no longer in the foreground.
                interval = TimeUnit.SECONDS.toMillis(15)

                // Sets the fastest rate for active location updates. This interval is exact,
                // and your application will never receive updates more frequently than this value.
                fastestInterval = TimeUnit.SECONDS.toMillis(10)

                // Sets the maximum time when batched location updates are delivered. Updates may be
                // delivered sooner than this interval.
                maxWaitTime = TimeUnit.SECONDS.toMillis(5)

                priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            }

    companion object {

        const val TASK_MOSHI: String = "task_moshi"
        const val FEED_MOSHI: String = "feed_moshi"
        const val SURVEY_MOSHI: String = "survey_moshi"

    }

}