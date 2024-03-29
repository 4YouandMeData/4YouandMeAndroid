package com.foryouandme.ui.permissions.compose

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
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.arch.toData
import com.foryouandme.core.ext.execute
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.permission.Permission
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.permissions.*
import com.foryouandme.ui.permissions.PermissionsAction.Initialize
import com.foryouandme.ui.permissions.PermissionsAction.RequestPermissions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PermissionsPage(
    permissionsViewModel: PermissionsViewModel = viewModel(),
    onBack: () -> Unit = {}
) {

    val state by permissionsViewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = "permissions") {
        permissionsViewModel.execute(Initialize)
    }

    LaunchedEffect(key1 = permissionsViewModel) {
        permissionsViewModel.eventsFlow
            .unwrapEvent("permissions")
            .onEach {
                when (it) {
                    PermissionsEvent.PermissionPermanentlyDenied -> {

                        val configuration = state.data.dataOrNull()?.configuration
                        if (configuration != null)
                            context.execute(
                                ContextAction.PermissionSettingsDialogAction(
                                    title = configuration.text.profile.permissions.denied,
                                    description = configuration.text.profile.permissions.message,
                                    settings = configuration.text.profile.permissions.settings,
                                    cancel = configuration.text.profile.permissions.cancel,
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
        onConfigurationError = { permissionsViewModel.execute(Initialize) }
    ) { configuration ->
        PermissionsPage(
            state = state,
            configuration = configuration,
            imageConfiguration = permissionsViewModel.imageConfiguration,
            onBack = onBack,
            onPermissionClicked = {
                if (it.isAllowed.not())
                    permissionsViewModel.execute(RequestPermissions(it.permission))
            }
        )
    }

}

@Composable
fun PermissionsPage(
    state: PermissionsState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
    onPermissionClicked: (PermissionItem) -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    Column(modifier = Modifier.fillMaxSize()) {
        ForYouAndMeTopAppBar(
            imageConfiguration = imageConfiguration,
            icon = TopAppBarIcon.Back,
            title = configuration.text.profile.permissions.title,
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
                    PermissionItem(
                        item = it,
                        configuration = configuration,
                        onItemClicked = onPermissionClicked
                    )
                }
            }
    }
}

@Preview
@Composable
fun PermissionsPagePreview() {
    ForYouAndMeTheme {
        PermissionsPage(
            state = PermissionsState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}