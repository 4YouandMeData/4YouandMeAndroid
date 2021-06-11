package com.foryouandme.core.arch.navigation

import com.foryouandme.ui.web.EWebPageType

interface NavigationAction

object AnywhereToAuth : NavigationAction

object AnywhereToWelcome : NavigationAction

data class AnywhereToWeb(
    val url: String,
    val type: EWebPageType = EWebPageType.OTHER
) : NavigationAction