package com.foryouandme.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.foryouandme.core.data.api.auth.response.UserResponse
import com.foryouandme.core.data.api.common.response.AnswerResponse
import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.core.data.api.common.response.QuestionResponse
import com.foryouandme.core.data.api.common.response.UnknownResourceResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityOptionResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedRewardResponse
import com.foryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import com.foryouandme.core.data.api.consent.review.response.ConsentReviewResponse
import com.foryouandme.core.data.api.consent.user.response.ConsentUserResponse
import com.foryouandme.core.data.api.feed.response.FeedResponse
import com.foryouandme.core.data.api.integration.response.IntegrationResponse
import com.foryouandme.core.data.api.optins.response.OptInsPermissionResponse
import com.foryouandme.core.data.api.optins.response.OptInsResponse
import com.foryouandme.core.data.api.screening.response.ScreeningResponse
import com.foryouandme.core.data.api.studyinfo.response.StudyInfoResponse
import com.foryouandme.data.repository.task.network.response.TaskResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import moe.banana.jsonapi2.ResourceAdapterFactory
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

    companion object {

        const val TASK_MOSHI: String = "task_moshi"

    }

}