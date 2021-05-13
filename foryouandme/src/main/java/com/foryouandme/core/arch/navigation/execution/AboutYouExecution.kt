package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesFragmentDirections
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuFragmentDirections

fun aboutYouMenuToReviewConsent(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuReviewConsent())
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

fun aboutYouMenuToPermissions(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToPermissions())
    }

fun aboutYouMenuPageToUserInfoPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouUserInfo())
    }

fun aboutYouMenuPageToDailySurveyTimePage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouDailySurveyTime())
    }