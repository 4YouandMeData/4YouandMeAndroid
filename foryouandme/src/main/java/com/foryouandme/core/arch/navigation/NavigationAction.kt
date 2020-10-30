package com.foryouandme.core.arch.navigation

interface NavigationAction

object AnywhereToAuth : NavigationAction

object AnywhereToWelcome : NavigationAction

data class AnywhereToWeb(val url: String) : NavigationAction
