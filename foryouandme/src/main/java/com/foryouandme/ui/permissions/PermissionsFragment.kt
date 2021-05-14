package com.foryouandme.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.permissions.compose.PermissionsPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsFragment : BaseFragment() {

    private val viewModel: PermissionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                PermissionsPage(
                    permissionsViewModel = viewModel,
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }

}