package com.foryouandme.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.arch.navigation.setupWithNavController
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.main.*

class MainFragment : BaseFragment<MainViewModel>(R.layout.main) {

    override val viewModel: MainViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { MainViewModel(navigator, injector.analyticsModule()) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fyamState {

            setupNavigation(it.configuration)

            viewModel.handleDeepLink(rootNavController(), it)

        }

    }

    private suspend fun setupNavigation(configuration: Configuration): Unit =
        evalOnMain {

            bottom_navigation.menu.clear()

            bottom_navigation.menu
                .add(Menu.NONE, R.id.feed_navigation, Menu.NONE, configuration.text.tab.feed)
                .setIcon(imageConfiguration.tabFeed())

            bottom_navigation.menu
                .add(Menu.NONE, R.id.tasks_navigation, Menu.NONE, configuration.text.tab.tasks)
                .setIcon(imageConfiguration.tabTask())

            bottom_navigation.menu
                .add(
                    Menu.NONE,
                    R.id.user_data_navigation,
                    Menu.NONE,
                    configuration.text.tab.userData
                )
                .setIcon(imageConfiguration.tabUserData())

            bottom_navigation.menu
                .add(
                    Menu.NONE,
                    R.id.study_info_navigation,
                    Menu.NONE,
                    configuration.text.tab.studyInfo
                )
                .setIcon(imageConfiguration.tabStudyInfo())

            bottom_navigation.itemIconTintList =
                selectedUnselectedColor(
                    configuration.theme.primaryTextColor.color(),
                    configuration.theme.secondaryMenuColor.color()
                )
            bottom_navigation.itemTextColor =
                selectedUnselectedColor(
                    configuration.theme.primaryTextColor.color(),
                    configuration.theme.secondaryMenuColor.color()
                )

            val navGraphIds = viewModel.getPagedIds()

            bottom_navigation.selectedItemId = viewModel.state().restorePage

            evalOnIO { viewModel.logBottomBarPageEvent(viewModel.state().restorePage) }

            // Setup the bottom navigation view with a list of navigation graphs
            bottom_navigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = childFragmentManager,
                containerId = R.id.main_nav_host_container,
                intent = requireActivity().intent
            ) {
                startCoroutineAsync {

                    viewModel.setRestorePage(it.itemId)
                    viewModel.logBottomBarPageEvent(it.itemId)

                }
            }
        }

}