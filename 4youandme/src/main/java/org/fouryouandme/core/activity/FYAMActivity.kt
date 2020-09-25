package org.fouryouandme.core.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fyam.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseActivity
import org.fouryouandme.core.arch.android.getSavedFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync

class FYAMActivity : BaseActivity<FYAMViewModel>(R.layout.fyam) {

    override val viewModel: FYAMViewModel by lazy {

        viewModelFactory(
            this,
            getSavedFactory(this) {
                FYAMViewModel(
                    navigator,
                    IORuntime,
                    it,
                    injector.configurationModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent {
                error.setError(it.error) { startCoroutineAsync { viewModel.initialize() } }
            }

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is FYAMStateUpdate.Config -> setupNavigation()
                }
            }

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize()

        }

    }

    private fun setupNavigation(): Unit {

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)
        navHostFragment.navController.graph = graph

    }

}
