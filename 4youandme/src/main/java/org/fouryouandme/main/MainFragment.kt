package org.fouryouandme.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.main.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.arch.navigation.setupWithNavController
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class MainFragment : BaseFragment<MainViewModel>(R.layout.main) {

    override val viewModel: MainViewModel by lazy {

        viewModelFactory(this, getFactory { MainViewModel(navigator, IORuntime) })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is MainStateUpdate.PageNavigation ->
                        bottom_navigation.selectedItemId = it.selectedPage
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration { evalOnMain { setupNavigation(it) } }

    }

    private fun setupNavigation(configuration: Configuration): Unit {

        bottom_navigation.menu.clear()

        bottom_navigation.menu
            .add(Menu.NONE, R.id.feed_navigation, Menu.NONE, configuration.text.tab.feed)
            .setIcon(imageConfiguration.tabFeed())

        bottom_navigation.menu
            .add(Menu.NONE, R.id.tasks_navigation, Menu.NONE, configuration.text.tab.tasks)
            .setIcon(imageConfiguration.tabTask())

        bottom_navigation.menu
            .add(Menu.NONE, R.id.user_data_navigation, Menu.NONE, configuration.text.tab.userData)
            .setIcon(imageConfiguration.tabUserData())

        bottom_navigation.menu
            .add(Menu.NONE, R.id.study_info_navigation, Menu.NONE, configuration.text.tab.studyInfo)
            .setIcon(imageConfiguration.tabStudyInfo())

        bottom_navigation.selectedItemId = viewModel.state().restorePage

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

        // Setup the bottom navigation view with a list of navigation graphs
        bottom_navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.main_nav_host_container,
            intent = requireActivity().intent
        )
    }

    override fun onDestroyView() {

        startCoroutineAsync { viewModel.setRestorePage(bottom_navigation.selectedItemId) }

        super.onDestroyView()
    }
}