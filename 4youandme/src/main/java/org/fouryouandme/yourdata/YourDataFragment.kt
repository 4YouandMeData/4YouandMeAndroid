package org.fouryouandme.yourdata

import android.os.Bundle
import android.view.View
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class YourDataFragment : BaseFragment<YourDataViewModel>(R.layout.your_data_page) {

    override val viewModel: YourDataViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                YourDataViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is YourDataStateUpdate.Initialization ->
                        configuration { applyConfiguration(it) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {
            if (viewModel.isInitialized().not()) {
                viewModel.initialize()

                applyConfiguration(viewModel.state().configuration)
            }
        }
    }

    private suspend fun applyConfiguration(configuration: Configuration) {
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

        }
    }
}