package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.aboutyou.appsanddevices.AboutYouAppsAndDevicesFragmentDirections
import com.fouryouandme.aboutyou.menu.AboutYouMenuFragmentDirections
import com.fouryouandme.core.arch.navigation.NavigationExecution

fun aboutYouMenuPageToAboutYouReviewConsentPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouReviewConsent())
    }

fun aboutYouMenuPageToAboutYouAppsAndDevicesPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouAppsAndDevices())
    }

fun aboutYouDataAppsAndDevicesToAboutYouIntegrationLogin(url: String): NavigationExecution =
    {
        it.navigate(
            AboutYouAppsAndDevicesFragmentDirections
                .actionAboutYouAppsAndDevicesToAboutYouIntegrationLogin(url)
        )
    }

fun aboutYouMenuPageToPermissionsPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouPermissions())
    }

fun aboutYouMenuPageToUserInfoPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouUserInfo())
    }