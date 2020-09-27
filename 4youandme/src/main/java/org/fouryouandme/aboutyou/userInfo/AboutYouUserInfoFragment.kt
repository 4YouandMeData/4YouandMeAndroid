package org.fouryouandme.aboutyou.userInfo

import android.os.Bundle
import kotlinx.android.synthetic.main.user_info.*
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.setStatusBar

class AboutYouUserInfoFragment :
    AboutYouSectionFragment<AboutYouUserInfoViewModel>(R.layout.user_info) {

    override val viewModel: AboutYouUserInfoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                AboutYouUserInfoViewModel(
                    navigator,
                    IORuntime
                )
            }
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        aboutYouViewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is AboutYouStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }

    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.primaryColorStart.color())

    }
}