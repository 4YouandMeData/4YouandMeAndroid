package com.foryouandme.ui.main

sealed class Screen(val route: String) {

    sealed class HomeScreen(
        route: String,
    ) : Screen(
        route,
    ) {
        object Feed : HomeScreen("feed")
        object Tasks : HomeScreen("tasks")
        object YourData : HomeScreen("your_data")
        object StudyInfo : HomeScreen("study_info")
    }

}