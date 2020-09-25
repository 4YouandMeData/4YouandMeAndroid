package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.aboutyou.appsanddevices.AboutYouAppsAndDevicesFragmentDirections
import org.fouryouandme.aboutyou.menu.AboutYouMenuFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun aboutYouMenuPageToAboutYouReviewConsentPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouReviewConsent())
    }

fun aboutYouMenuPageToAboutYouAppsAndDevicesPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouAppsAndDevices())
    }

fun aboutYouDataAppsAndDevicesToIntegrationLogin(url: String): NavigationExecution =
    {
        it.navigate(AboutYouAppsAndDevicesFragmentDirections.actionAboutYouAppsAndDevicesToIntegrationLogin(url, null))
    }