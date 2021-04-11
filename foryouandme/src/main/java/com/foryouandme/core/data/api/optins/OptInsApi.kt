package com.foryouandme.core.data.api.optins

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.consent.network.request.OptInPermissionRequest
import com.foryouandme.data.repository.auth.consent.network.response.OptInsResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.*

interface OptInsApi {

    @GET("api/v1/studies/{study_id}/opt_in")
    suspend fun getOptIns(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<OptInsResponse>


    @POST("api/v1/permissions/{permission_id}/user_permission")
    suspend fun setPermission(
        @Header(Headers.AUTH) token: String,
        @Path("permission_id") permissionId: String,
        @Body request: OptInPermissionRequest
    )

}