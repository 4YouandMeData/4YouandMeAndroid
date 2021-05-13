package com.foryouandme.ui.aboutyou.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.permissions.compose.PermissionsPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsFragment : AboutYouSectionFragment() {

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
                    onBack = { back() }
                )
            }
        }

}