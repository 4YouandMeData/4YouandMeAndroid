package org.fouryouandme.auth.welcome

import android.os.Bundle
import kotlinx.android.synthetic.main.welcome.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.theme.Theme
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class WelcomeFragment : BaseFragment<WelcomeViewModel>(R.layout.welcome) {

    override val viewModel: WelcomeViewModel by lazy {
        viewModelFactory(this, getFactory { WelcomeViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when(it) {
                    is WelcomeStateUpdate.Initialization -> applyTheme(it.theme)
                }
            }

        viewModel.initialize()
    }

    private fun applyTheme(theme: Theme): Unit {

        root.background = theme.primaryGradient.drawable()
        //root.setBackgroundColor(R.color.design_default_color_primary)
    }
}