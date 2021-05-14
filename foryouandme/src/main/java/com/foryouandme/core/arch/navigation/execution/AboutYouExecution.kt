package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.aboutyou.AboutYouFragmentDirections
import com.foryouandme.ui.appsanddevices.AppsAndDevicesFragmentDirections

fun aboutYouToReviewConsent(): NavigationExecution =
    {
        it.navigate(AboutYouFragmentDirections.actionAboutYouToReviewConsent())
    }

fun aboutYouToAppsAndDevices(): NavigationExecution =
    {
        it.navigate(AboutYouFragmentDirections.actionAboutYouToAppsAndDevices())
    }

fun appsAndDevicesToIntegrationLogin(url: String): NavigationExecution =
    {
        it.navigate(
            AppsAndDevicesFragmentDirections.actionAppsAndDevicesToIntegrationLogin(url)
        )
    }

fun aboutYouToPermissions(): NavigationExecution =
    {
        it.navigate(AboutYouFragmentDirections.actionAboutYouToPermissions())
    }

fun aboutYouToUserInfo(): NavigationExecution =
    {
        it.navigate(AboutYouFragmentDirections.actionAboutYouToUserInfo())
    }

fun aboutYouToDailySurveyTime(): NavigationExecution =
    {
        it.navigate(AboutYouFragmentDirections.actionMenuToDailySurveyTime())
    }