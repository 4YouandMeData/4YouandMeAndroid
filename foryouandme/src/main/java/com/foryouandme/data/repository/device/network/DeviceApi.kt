package com.foryouandme.data.repository.device.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.device.network.request.DeviceInfoRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeviceApi {

    @POST("/api/v1/phone_events")
    suspend fun sendDeviceInfo(
        @Header(Headers.AUTH) token: String,
        @Body request: DeviceInfoRequest
    )

}