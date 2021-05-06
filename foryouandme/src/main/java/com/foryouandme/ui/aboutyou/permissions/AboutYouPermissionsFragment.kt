package com.foryouandme.ui.aboutyou.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.permissions.compose.AboutYouPermissionsPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutYouPermissionsFragment : AboutYouSectionFragment() {

    private val viewModel: AboutYouPermissionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                AboutYouPermissionsPage(
                    aboutYouPermissionsViewModel = viewModel,
                    onBack = { back() }
                )
            }
        }

}