package com.foryouandme.ui.userInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.userInfo.compose.UserInfoPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : BaseFragment() {

    private val viewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                UserInfoPage(
                    userInfoViewModel = viewModel,
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }

}