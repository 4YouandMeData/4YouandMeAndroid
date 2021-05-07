package com.foryouandme.ui.aboutyou.permissions.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.R
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.arch.toData
import com.foryouandme.core.ext.execute
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.permission.Permission
import com.foryouandme.ui.aboutyou.permissions.*
import com.foryouandme.ui.aboutyou.permissions.AboutYouPermissionsAction.Initialize
import com.foryouandme.ui.aboutyou.permissions.AboutYouPermissionsAction.RequestPermissions
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun AboutYouPermissionsPage(
    aboutYouPermissionsViewModel: AboutYouPermissionsViewModel = viewModel(),
    onBack: () -> Unit = {}
) {

    val state by aboutYouPermissionsViewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = aboutYouPermissionsViewModel) {
        aboutYouPermissionsViewModel.events
            .onEach {
                when (it) {
                    AboutYouPermissionsEvent.PermissionPermanentlyDenied -> {

                        val configuration = state.data.orNull()?.configuration
                        if (configuration != null)
                            context.execute(
                                ContextAction.PermissionSettingsDialogAction(
                                    title = configuration.text.profile.permissionDenied,
                                    description = configuration.text.profile.permissionMessage,
                                    settings = configuration.text.profile.permissionSettings,
                                    cancel = configuration.text.profile.permissionCancel,
                                    isCancelable = true
                                )
                            )
                    }
                }
            }
            .collect()
    }

    ForYouAndMeTheme(
        configuration = state.data.map { it.configuration },
        onConfigurationError = { aboutYouPermissionsViewModel.execute(Initialize) }
    ) { configuration ->
        AboutYouPermissionsPage(
            state = state,
            configuration = configuration,
            imageConfiguration = aboutYouPermissionsViewModel.imageConfiguration,
            onBack = onBack,
            onPermissionClicked = {
                if (it.isAllowed.not())
                    aboutYouPermissionsViewModel.execute(RequestPermissions(it.permission))
            }
        )
    }

}

@Composable
fun AboutYouPermissionsPage(
    state: AboutYouPermissionsState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
    onPermissionClicked: (PermissionsItem) -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value) {
        Column(modifier = Modifier.fillMaxSize()) {
            ForYouAndMeTopAppBar(
                imageConfiguration = imageConfiguration,
                icon = TopAppBarIcon.Back,
                title = configuration.text.profile.fourthItem,
                titleColor = configuration.theme.secondaryColor.value,
                modifier =
                Modifier
                    .height(110.dp)
                    .background(configuration.theme.verticalGradient),
                onBack = onBack
            )
            if (state.data is LazyData.Data)
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 50.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(configuration.theme.secondaryColor.value)
                ) {
                    items(state.data.value.permissions) {
                        AboutYouPermission(
                            item = it,
                            configuration = configuration,
                            onItemClicked = onPermissionClicked
                        )
                    }
                }
        }
    }
}

@Preview
@Composable
fun AboutYouPermissionsPagePreview() {
    ForYouAndMeTheme {
        AboutYouPermissionsPage(
            state =
            AboutYouPermissionsState(
                data =
                AboutYouPermissionsData(
                    permissions = listOf(
                        PermissionsItem(
                            configuration = Configuration.mock(),
                            "",
                            Permission.Location,
                            Mock.body,
                            R.drawable.placeholder,
                            true
                        )
                    ),
                    configuration = Configuration.mock()
                ).toData()
            ),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}