package com.foryouandme.core.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseActivity
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.fyam.*

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
            .observeEvent {
                when(it.task) {
                    FYAMLoading.Config -> loading.setVisibility(it.active, false)
                    FYAMLoading.Splash -> {

                        if(it.active) {
                            splash_logo.alpha = 1f
                            splash_logo.isVisible = true
                        }
                        else {
                            splash_logo.animate()
                                .alpha(0f)
                                .withEndAction { splash_logo.isVisible = false }
                                .start()

                        }

                    }
                }
            }

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
                    openAppIntegrationArg(),
                    true
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
