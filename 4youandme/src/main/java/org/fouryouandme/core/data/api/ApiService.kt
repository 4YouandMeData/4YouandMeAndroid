package org.fouryouandme.core.data.api

import arrow.integrations.retrofit.adapter.CallKindAdapterFactory
import com.squareup.moshi.Moshi
import moe.banana.jsonapi2.JsonApiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

inline fun <reified T> getApiService(
    baseUrl: String,
    moshi: Moshi
): T {

    val httpClient = OkHttpClient.Builder()

    // TODO: enable only for staging
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    httpClient.addInterceptor(logger)

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JsonApiConverterFactory.create(moshi))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CallKindAdapterFactory.create())
        .client(httpClient.build())
        .build()
        .create(T::class.java)
}
