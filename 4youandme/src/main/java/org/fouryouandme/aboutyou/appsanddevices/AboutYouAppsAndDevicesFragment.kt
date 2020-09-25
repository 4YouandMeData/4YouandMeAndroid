package org.fouryouandme.aboutyou.appsanddevices

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.about_you_menu.root
import kotlinx.android.synthetic.main.apps_and_devices.*
import kotlinx.android.synthetic.main.html_detail.*
import kotlinx.android.synthetic.main.html_detail.backArrow
import kotlinx.android.synthetic.main.html_detail.detailsToolbar
import kotlinx.android.synthetic.main.html_detail.title
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouAppsAndDevicesFragment :
    AboutYouSectionFragment<AboutYouAppsAndDevicesViewModel>(R.layout.apps_and_devices) {
    override val viewModel: AboutYouAppsAndDevicesViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                AboutYouAppsAndDevicesViewModel(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (aboutYouViewModel.isInitialized()) {
            applyConfiguration(aboutYouViewModel.state().configuration)
        }
    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        detailsToolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        backArrow.setImageResource(imageConfiguration.back())
        backArrow.setOnClickListener {

            startCoroutineAsync {
                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
            }
        }

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.profile.secondItem

        firstItem.applyData(
            configuration,
            requireContext().imageConfiguration.fitbit(),
            "Garmin",
            ""
        )

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.oura(),
            "Oura",
            ""
        )


    }
}