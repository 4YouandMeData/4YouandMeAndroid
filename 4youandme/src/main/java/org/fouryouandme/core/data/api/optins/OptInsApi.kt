package org.fouryouandme.core.data.api.optins

import arrow.integrations.retrofit.adapter.CallK
import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.optins.request.OptInPermissionRequest
import org.fouryouandme.core.data.api.optins.response.OptInsResponse
import retrofit2.http.*

interface OptInsApi {

    @GET("api/v1/studies/{study_id}/opt_in")
    fun getOptIns(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): CallK<ObjectDocument<OptInsResponse>>


    @POST("api/v1/permissions/{permission_id}/user_permission")
    fun setPermission(
        @Header(Headers.AUTH) token: String,
        @Path("permission_id") permissionId: String,
        @Body request: OptInPermissionRequest
    ): CallK<Unit>

}