package com.foryouandme.ui.appsanddevices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.appsanddevices.compose.AppsAndDevicesPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppsAndDevicesFragment : BaseFragment() {

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
                                rootNavController(),
                                AppsAndDevicesToIntegrationLogin(it.disconnectLink)
                            )
                        else
                            navigator.navigateTo(
                                rootNavController(),
                                AppsAndDevicesToIntegrationLogin(it.connectLink)
                            )
                    },
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }
}