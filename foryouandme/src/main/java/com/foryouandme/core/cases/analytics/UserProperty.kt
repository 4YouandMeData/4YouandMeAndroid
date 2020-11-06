package com.foryouandme.core.cases.analytics

sealed class UserProperty(val name: String) {

    object DeviceId: UserProperty("device_id")

}