package org.fouryouandme.aboutyou

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.screening.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync

class AboutYouFragment : BaseFragment<AboutYouViewModel>(R.layout.about_you) {

    override val viewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouViewModel(
                    navigator,
                    IORuntime,
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
                        { view ->
                            view.hide()
                            startCoroutineAsync {
                                viewModel.initialize(rootNavController(), true)
                            }
                        }
                    AboutYouError.Refresh ->
                        error.setError(payload.error)
                        { view ->
                            view.hide()
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