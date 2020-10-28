package org.fouryouandme.core.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fyam.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseActivity
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync

class FYAMActivity : BaseActivity<FYAMViewModel>(R.layout.fyam) {

    override val viewModel: FYAMViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                FYAMViewModel(
                    navigator,
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
                error.setError(it.error) {
                    startCoroutineAsync {
                        viewModel.initialize(
                            rootNavController(),
                            taskIdArg(),
                            urlArg(),
                            openAppIntegrationArg()
                        )
                    }
                }
            }

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is FYAMStateUpdate.Config -> setupNavigation()
                }
            }

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(
                    rootNavController(),
                    taskIdArg(),
                    urlArg(),
                    openAppIntegrationArg()
                )

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

    fun taskIdArg(): String? = intent.extras?.getString(TASK_ID)
    fun urlArg(): String? = intent.extras?.getString(URL)
    fun openAppIntegrationArg(): String? = intent.extras?.getString(OPEN_APP_INTEGRATION)

    companion object {

        private const val TASK_ID = "task_id"

        private const val URL = "url"

        private const val OPEN_APP_INTEGRATION = "open_app_integration"

        fun getIntent(
            context: Context,
            args: Map<String, String>
        ): Intent =
            getIntent(
                context,
                args[TASK_ID],
                args[URL],
                args[OPEN_APP_INTEGRATION]
            )

        fun getIntent(
            context: Context,
            taskId: String?,
            url: String?,
            openAppIntegration: String?
        ): Intent =
            Intent(context, FYAMActivity::class.java)
                .apply {

                    putExtra(TASK_ID, taskId)
                    putExtra(URL, url)
                    putExtra(OPEN_APP_INTEGRATION, openAppIntegration)

                }

    }

}
