package com.foryouandme.domain.usecase.permission

import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

class RequestPermissionUseCase @Inject constructor(
    private val repository: PermissionRepository,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) {

    suspend operator fun invoke(permission: Permission): PermissionResult {

        val result = repository.requestPermission(permission)

        GlobalScope.launchSafe { // log event, ignore errors

            sendAnalyticsEventUseCase(
                AnalyticsEvent.LocationPermissionChanged(result is PermissionResult.Granted),
                EAnalyticsProvider.ALL
            )

        }

        return result

    }

}