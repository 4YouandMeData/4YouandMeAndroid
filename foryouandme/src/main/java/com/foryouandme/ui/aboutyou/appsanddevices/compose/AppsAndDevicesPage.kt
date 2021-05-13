package com.foryouandme.ui.aboutyou.appsanddevices.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesAction.GetConfiguration
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesAction.GetIntegrations
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesState
import com.foryouandme.ui.aboutyou.appsanddevices.AppsAndDevicesViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient

@Composable
fun AppsAndDevicesPage(
    appsAndDevicesViewModel: AppsAndDevicesViewModel = viewModel(),
    onAppsAndDeviceItemClicked: (AppsAndDeviceItem) -> Unit = {},
    onBack: () -> Unit = {}
) {

    val state by appsAndDevicesViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { appsAndDevicesViewModel.execute(GetConfiguration) }
    ) { configuration ->
        AppsAndDevicesPage(
            state = state,
            configuration = configuration,
            imageConfiguration = appsAndDevicesViewModel.imageConfiguration,
            onAppsAndDevicesError = { appsAndDevicesViewModel.execute(GetIntegrations) },
            onAppsAndDeviceItemClicked = onAppsAndDeviceItemClicked,
            onBack = onBack,
        )
    }

}

@Composable
fun AppsAndDevicesPage(
    state: AppsAndDevicesState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onAppsAndDevicesError: () -> Unit = {},
    onAppsAndDeviceItemClicked: (AppsAndDeviceItem) -> Unit = {},
    onBack: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value) {
        LoadingError(
            data = state.appsAndDevices,
            configuration = configuration,
            onRetryClicked = onAppsAndDevicesError
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {
                ForYouAndMeTopAppBar(
                    imageConfiguration = imageConfiguration,
                    icon = TopAppBarIcon.Back,
                    title = configuration.text.profile.secondItem,
                    titleColor = configuration.theme.secondaryColor.value,
                    modifier =
                    Modifier
                        .height(110.dp)
                        .background(configuration.theme.verticalGradient),
                    onBack = onBack
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 50.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(configuration.theme.secondaryColor.value)
                ) {
                    items(it) {
                        AppsAndDeviceItem(
                            item = it,
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onItemClicked = onAppsAndDeviceItemClicked
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppsAndDevicesPreview() {
    ForYouAndMeTheme {
        AppsAndDevicesPage(
            state = AppsAndDevicesState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}