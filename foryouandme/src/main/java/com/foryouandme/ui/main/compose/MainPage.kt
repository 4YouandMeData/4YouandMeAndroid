package com.foryouandme.ui.main.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.ext.execute
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.main.MainEvent
import com.foryouandme.ui.main.MainViewModel
import com.foryouandme.ui.main.Screen
import com.foryouandme.ui.main.compose.items.FeedItem
import com.foryouandme.ui.main.feeds.compose.FeedPage
import com.foryouandme.ui.main.tasks.compose.TasksPage
import com.foryouandme.ui.studyinfo.compose.StudyInfoPage
import com.foryouandme.ui.yourdata.compose.YourDataPage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainPage(
    viewModel: MainViewModel,
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {},
    onLogoClicked: () -> Unit = {},
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
    openTask: (String) -> Unit = {},
    openUrl: (String) -> Unit = {},
) {

    val context = LocalContext.current
    val state = viewModel.stateFlow.collectAsState()

    LaunchedEffect(key1 = viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("main")
            .onEach {
                when(it) {
                    is MainEvent.OpenApp -> context.execute(ContextAction.OpenApp(it.packageName))
                    is MainEvent.OpenTask -> openTask(it.taskId)
                    is MainEvent.OpenUrl -> openUrl(it.url)
                }
            }
            .collect()
    }

    ForYouAndMeTheme(configuration = state.value.configuration) {
        MainPage(
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onTaskActivityClicked = onTaskActivityClicked,
            onFeedActionClicked = onFeedActionClicked,
            onLogoClicked = onLogoClicked,
            onAboutYouClicked = onAboutYouClicked,
            onContactClicked = onContactClicked,
            onRewardsClicked = onRewardsClicked,
            onFAQClicked = onFAQClicked
        )
    }

}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun MainPage(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {},
    onLogoClicked: () -> Unit = {},
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val bottomBar: @Composable () -> Unit = {
        BottomBar(
            configuration = configuration,
            imageConfiguration = imageConfiguration,
            navController = navController,
            screens =
            listOf(
                Screen.HomeScreen.Feed,
                Screen.HomeScreen.Tasks,
                Screen.HomeScreen.YourData,
                Screen.HomeScreen.StudyInfo
            ),
            modifier = Modifier.height(60.dp)
        )
    }

    Scaffold(
        bottomBar = { bottomBar() },
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        NavigationHost(
            paddingValues = paddingValues,
            navController = navController,
            onTaskActivityClicked = onTaskActivityClicked,
            onFeedActionClicked = {
                when (it) {
                    FeedAction.Feed -> {
                    }
                    FeedAction.Tasks ->
                        navController.navigateToHomeScreen(Screen.HomeScreen.Tasks)
                    FeedAction.YourData ->
                        navController.navigateToHomeScreen(Screen.HomeScreen.YourData)
                    FeedAction.StudyInfo ->
                        navController.navigateToHomeScreen(Screen.HomeScreen.StudyInfo)
                    else -> onFeedActionClicked(it)
                }
            },
            onLogoClicked = onLogoClicked,
            onFeedButtonClicked = { navController.navigateToHomeScreen(Screen.HomeScreen.Feed) },
            onAboutYouClicked = onAboutYouClicked,
            onContactClicked = onContactClicked,
            onRewardsClicked = onRewardsClicked,
            onFAQClicked = onFAQClicked
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun NavigationHost(
    paddingValues: PaddingValues,
    navController: NavController,
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {},
    onLogoClicked: () -> Unit = {},
    onFeedButtonClicked: () -> Unit = {},
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.HomeScreen.Feed.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.HomeScreen.Feed.route) {
            FeedPage(
                feedsViewModel = hiltViewModel(),
                onTaskActivityClicked = onTaskActivityClicked,
                onFeedActionClicked = onFeedActionClicked,
                onLogoClicked = onLogoClicked
            )
        }
        composable(Screen.HomeScreen.Tasks.route) {
            TasksPage(
                tasksViewModel = hiltViewModel(),
                onFeedButtonClicked = onFeedButtonClicked,
                onTaskActivityClicked = onTaskActivityClicked,
            )
        }
        composable(Screen.HomeScreen.YourData.route) { YourDataPage(hiltViewModel()) }
        composable(Screen.HomeScreen.StudyInfo.route) {
            StudyInfoPage(
                studyInfoViewModel = hiltViewModel(),
                onAboutYouClicked = onAboutYouClicked,
                onContactClicked = onContactClicked,
                onRewardsClicked = onRewardsClicked,
                onFAQClicked = onFAQClicked
            )
        }
    }
}

private fun NavHostController.navigateToHomeScreen(screen: Screen.HomeScreen) {
    navigate(screen.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}