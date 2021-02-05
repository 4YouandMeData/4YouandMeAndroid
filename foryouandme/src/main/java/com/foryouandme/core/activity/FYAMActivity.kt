package com.foryouandme.core.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseActivity
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.navigation.AnywhereToAuth
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.FyamBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FYAMActivity : BaseActivity() {

    private val viewModel: FYAMViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FyamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loading
            .onEach {
                when (it.task) {
                    FYAMLoading.Config -> binding.loading.setVisibility(it.active, false)
                    FYAMLoading.Splash -> {

                        if (it.active) {
                            binding.splashLogo.alpha = 1f
                            binding.splashLogo.isVisible = true
                        } else {
                            binding.splashLogo.animate()
                                .alpha(0f)
                                .withEndAction { binding.splashLogo.isVisible = false }
                                .start()

                        }

                    }
                }
            }
            .observeIn(this)

        viewModel.error
            .onEach {
                binding.error.setError(it.error, viewModel.state.configuration) {

                    viewModel.execute(
                        FYAMStateEvent.Initialize(
                            taskIdArg(),
                            urlArg(),
                            openAppIntegrationArg()
                        )
                    )

                }
            }
            .observeIn(this)

        viewModel.stateUpdate
            .onEach {
                when (it) {
                    is FYAMStateUpdate.Config -> setupNavigation()
                }
            }
            .observeIn(this)

        viewModel.execute(
            FYAMStateEvent.Initialize(
                taskIdArg(),
                urlArg(),
                openAppIntegrationArg(),
                true
            )
        )

    }

    private fun setupNavigation() {

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.navigation)
            navHostFragment.navController.graph = graph
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(FYAMStateEvent.SendDeviceInfo)

    }

    private fun taskIdArg(): String? = intent.extras?.getString(TASK_ID)
    private fun urlArg(): String? = intent.extras?.getString(URL)
    private fun openAppIntegrationArg(): String? = intent.extras?.getString(OPEN_APP_INTEGRATION)

    fun auth() {

        navigator.navigateTo(rootNavController(), AnywhereToAuth)

    }

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

        private fun getIntent(
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
