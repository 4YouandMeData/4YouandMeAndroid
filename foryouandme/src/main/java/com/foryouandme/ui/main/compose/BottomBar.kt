package com.foryouandme.ui.main.compose

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.main.Screen

@Composable
fun BottomBar(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    modifier: Modifier = Modifier,
    screens: List<Screen.HomeScreen>,
    navController: NavController
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = configuration.theme.secondaryColor.value
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter =
                        painterResource(
                            id =
                            when (screen) {
                                Screen.HomeScreen.Feed -> imageConfiguration.tabFeed()
                                Screen.HomeScreen.Tasks -> imageConfiguration.tabTask()
                                Screen.HomeScreen.YourData -> imageConfiguration.tabUserData()
                                Screen.HomeScreen.StudyInfo -> imageConfiguration.tabStudyInfo()

                            }
                        ),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text =
                        when (screen) {
                            Screen.HomeScreen.Feed -> configuration.text.tab.feed
                            Screen.HomeScreen.Tasks -> configuration.text.tab.tasks
                            Screen.HomeScreen.YourData -> configuration.text.tab.userData
                            Screen.HomeScreen.StudyInfo -> configuration.text.tab.studyInfo
                        }
                    )
                },
                selectedContentColor = configuration.theme.primaryColorEnd.value,
                unselectedContentColor = configuration.theme.primaryTextColor.value,
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}