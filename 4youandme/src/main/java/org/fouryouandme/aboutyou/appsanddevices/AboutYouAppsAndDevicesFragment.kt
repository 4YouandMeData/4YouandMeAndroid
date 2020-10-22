package org.fouryouandme.aboutyou.appsanddevices

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.about_you_menu.root
import kotlinx.android.synthetic.main.apps_and_devices.*
import kotlinx.android.synthetic.main.html_detail.backArrow
import kotlinx.android.synthetic.main.html_detail.detailsToolbar
import kotlinx.android.synthetic.main.html_detail.title
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
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
                AboutYouAppsAndDevicesViewModel(navigator)
            }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(AppAndDeviceViewHolder.factory {

            if (it.isConnected) {
                startCoroutineAsync {
                    viewModel.navigateToWeb(it.link, aboutYouNavController())
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            setupRecyclerView()
            applyConfiguration(it)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshUserAndConfiguration { config, user ->

            val integrations = config.getStudyIntegrations()
                .map {
                    when (it) {
                        StudyIntegration.Garmin -> viewModel.createGarminItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.Garmin).not()
                        )

                        StudyIntegration.Fitbit -> viewModel.createFitbitItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.Fitbit).not()
                        )

                        StudyIntegration.Oura -> viewModel.createOuraItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.Oura).not()
                        )

                        StudyIntegration.Instagram -> viewModel.createInstagramItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.Instagram).not()
                        )

                        StudyIntegration.RescueTime -> viewModel.createRescueTimeItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.RescueTime).not()
                        )

                        StudyIntegration.Twitter -> viewModel.createTwitterItem(
                            config,
                            imageConfiguration,
                            user.identities.contains(StudyIntegration.Twitter).not()
                        )
                    }
                }

            adapter.submitList(integrations)
        }
    }

    private suspend fun applyConfiguration(configuration: Configuration) =
        evalOnMain {

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

        }

    private suspend fun setupRecyclerView() =
        evalOnMain {

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            recycler_view.adapter = adapter

            recycler_view.addItemDecoration(
                LinearMarginItemDecoration(
                    {
                        if (it.index == 0) 50.dpToPx()
                        else 30.dpToPx()
                    },
                    { 20.dpToPx() },
                    { 20.dpToPx() },
                    {
                        if (it.index == it.itemCount) 30.dpToPx()
                        else 0.dpToPx()
                    }
                )
            )

        }
}