package com.foryouandme.ui.aboutyou

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.screening.*

class AboutYouFragment : BaseFragmentOld<AboutYouViewModel>(R.layout.about_you) {

    override val viewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouViewModel(
                    navigator,
                    injector.authModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { update ->
                when (update) {
                    is AboutYouStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    AboutYouLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                    AboutYouLoading.Refresh ->
                        loading.setVisibility(it.active, true)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    AboutYouError.Initialization ->
                        error.setError(payload.error)
                        {
                            startCoroutineAsync {
                                viewModel.initialize(rootNavController(), true)
                            }
                        }
                    AboutYouError.Refresh ->
                        error.setError(payload.error)
                        {
                            startCoroutineAsync {
                                viewModel.refreshUser(rootNavController(), true)
                            }
                        }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), true)

        }
    }

    private fun setupNavigation(): Unit {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.about_you_navigation)
        navHostFragment.navController.graph = graph

    }

}