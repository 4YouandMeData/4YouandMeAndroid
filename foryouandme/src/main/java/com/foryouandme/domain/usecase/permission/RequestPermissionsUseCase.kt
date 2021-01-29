package com.foryouandme.domain.usecase.permission

import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

class RequestPermissionsUseCase @Inject constructor(
    private val repository: PermissionRepository,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) {

    suspend operator fun invoke(permission: List<Permission>): List<PermissionResult> {

        val results = repository.requestPermissions(permission)

        results.firstOrNull { it.permission == Permission.Location }
            ?.let {

                GlobalScope.launchSafe { // log event, ignore errors

                    sendAnalyticsEventUseCase(
                        AnalyticsEvent.LocationPermissionChanged(it is PermissionResult.Granted),
                        EAnalyticsProvider.ALL
                    )

                }
            }

        return results

    }

}