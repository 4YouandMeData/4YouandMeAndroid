package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.aboutyou.appsanddevices.AboutYouAppsAndDevicesFragmentDirections
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuFragmentDirections

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

fun aboutYouMenuPageToDailySurveyTimePage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouDailySurveyTime())
    }