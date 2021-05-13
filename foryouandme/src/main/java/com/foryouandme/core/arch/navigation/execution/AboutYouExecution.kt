package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesFragmentDirections
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuFragmentDirections

fun aboutYouMenuPageToAboutYouReviewConsentPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouReviewConsent())
    }

fun aboutYouMenuToAppsAndDevices(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAppsAndDevices())
    }

fun appsAndDevicesToIntegrationLogin(url: String): NavigationExecution =
    {
        it.navigate(
            AppsAndDevicesFragmentDirections.actionAppsAndDevicesToIntegrationLogin(url)
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