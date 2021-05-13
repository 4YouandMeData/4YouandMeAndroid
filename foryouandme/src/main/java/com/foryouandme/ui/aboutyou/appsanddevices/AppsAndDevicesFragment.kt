package com.foryouandme.ui.aboutyou.appsanddevices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.AppsAndDevicesToIntegrationLogin
import com.foryouandme.ui.aboutyou.appsanddevices.compose.AppsAndDevicesPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppsAndDevicesFragment : AboutYouSectionFragment() {

    private val viewModel: AppsAndDevicesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                AppsAndDevicesPage(
                    appsAndDevicesViewModel = viewModel,
                    onAppsAndDeviceItemClicked = {
                        if (it.isConnected)
                            navigator.navigateTo(
                                aboutYouNavController(),
                                AppsAndDevicesToIntegrationLogin(it.disconnectLink)
                            )
                        else
                            navigator.navigateTo(
                                aboutYouNavController(),
                                AppsAndDevicesToIntegrationLogin(it.connectLink)
                            )
                    },
                    onBack = { back() }
                )
            }
        }
}