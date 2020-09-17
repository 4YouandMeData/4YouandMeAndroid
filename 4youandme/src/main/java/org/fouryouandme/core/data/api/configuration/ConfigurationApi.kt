package org.fouryouandme.core.data.api.configuration

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.configuration.response.ConfigurationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfigurationApi {

    @Deprecated("use suspend version")
    @GET("/api/v1/studies/{study_id}/configuration")
    fun getConfiguration(@Path("study_id") studyId: String): CallK<ConfigurationResponse>

    @GET("/api/v1/studies/{study_id}/configuration")
    suspend fun getConfigurationFx(@Path("study_id") studyId: String): ConfigurationResponse

}