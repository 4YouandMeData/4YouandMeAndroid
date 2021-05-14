package com.foryouandme.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.activity.FYAMViewModel
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.setupWithNavController
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.selectedUnselectedColor
import com.foryouandme.databinding.MainBinding
import com.foryouandme.entity.configuration.Configuration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.main) {

    val viewModel: MainViewModel by viewModels()

    private val fyamViewModel: FYAMViewModel by viewModels({ requireActivity() })

    private val binding: MainBinding?
        get() = view?.let { MainBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    MainLoading.Config ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {

                binding?.loading?.setVisibility(false)

                when (it.cause) {
                    MainError.Config ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(MainStateEvent.GetConfig) }
                }
            }
            .observeIn(this)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is MainStateUpdate.PageNavigation ->
                        binding?.bottomNavigation?.selectedItemId = it.selectedPage
                    is MainStateUpdate.Config ->
                        setupView(it.configuration)
                    else ->
                        Unit
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach { navigator.navigateTo(rootNavController(), it) }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val configuration = viewModel.state.configuration

        if (configuration == null) viewModel.execute(MainStateEvent.GetConfig)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            (v.layoutParams as MarginLayoutParams).bottomMargin = insets.systemWindowInsetBottom
            insets.consumeSystemWindowInsets()
        }

    }

    private fun setupView(configuration: Configuration) {

        setupNavigation(configuration)
        viewModel.execute(MainStateEvent.HandleDeepLink(fyamViewModel.state))

    }

    private fun setupNavigation(configuration: Configuration) {

        val viewBinding = binding

        viewBinding?.bottomNavigation?.menu?.clear()

        viewBinding?.bottomNavigation?.menu
            ?.add(Menu.NONE, R.id.feed_navigation, Menu.NONE, configuration.text.tab.feed)
            ?.setIcon(imageConfiguration.tabFeed())

        viewBinding?.bottomNavigation?.menu
            ?.add(Menu.NONE, R.id.tasks_navigation, Menu.NONE, configuration.text.tab.tasks)
            ?.setIcon(imageConfiguration.tabTask())

        viewBinding?.bottomNavigation?.menu
            ?.add(
                Menu.NONE,
                R.id.user_data_navigation,
                Menu.NONE,
                configuration.text.tab.userData
            )
            ?.setIcon(imageConfiguration.tabUserData())

        viewBinding?.bottomNavigation?.menu
            ?.add(
                Menu.NONE,
                R.id.study_info_navigation,
                Menu.NONE,
                configuration.text.tab.studyInfo
            )
            ?.setIcon(imageConfiguration.tabStudyInfo())

        viewBinding?.bottomNavigation?.itemIconTintList =
            selectedUnselectedColor(
                configuration.theme.primaryTextColor.color(),
                configuration.theme.secondaryMenuColor.color()
            )
        viewBinding?.bottomNavigation?.itemTextColor =
            selectedUnselectedColor(
                configuration.theme.primaryTextColor.color(),
                configuration.theme.secondaryMenuColor.color()
            )

        viewBinding?.bottomNavigation?.setBackgroundColor(
            configuration.theme.secondaryColor.color()
        )

        val navGraphIds = viewModel.getPagedIds()

        viewBinding?.bottomNavigation?.selectedItemId = viewModel.state.restorePage

        viewModel.execute(MainStateEvent.LoadPageSelected(viewModel.state.restorePage))

        // Setup the bottom navigation view with a list of navigation graphs
        viewBinding?.bottomNavigation?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.main_nav_host_container,
            intent = requireActivity().intent
        ) {

            viewModel.execute(MainStateEvent.SetRestorePage(it.itemId))
            viewModel.execute(MainStateEvent.LoadPageSelected(it.itemId))

        }
    }

}