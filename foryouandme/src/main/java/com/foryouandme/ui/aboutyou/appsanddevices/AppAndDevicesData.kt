package com.foryouandme.ui.aboutyou.appsanddevices

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.aboutyou.appsanddevices.compose.AppsAndDeviceItem

data class AppsAndDevicesState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val appsAndDevices: LazyData<List<AppsAndDeviceItem>> = LazyData.Empty
) {

    companion object {

        fun mock(): AppsAndDevicesState =
            AppsAndDevicesState(
                configuration = Configuration.mock().toData(),
                appsAndDevices =
                listOf(
                    AppsAndDeviceItem.mock(),
                    AppsAndDeviceItem.mock(),
                    AppsAndDeviceItem.mock()
                ).toData()
            )

    }

}

sealed class AppsAndDevicesAction {

    object GetConfiguration : AppsAndDevicesAction()
    object GetIntegrations : AppsAndDevicesAction()
    object ScreenViewed : AppsAndDevicesAction()

}