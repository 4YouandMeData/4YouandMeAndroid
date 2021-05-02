package com.foryouandme.ui.aboutyou.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.menu.compose.AboutYouMenuPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutYouMenuFragment : AboutYouSectionFragment() {

    private val viewModel: AboutYouMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                AboutYouMenuPage(viewModel) { back() }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupView()
        //applyConfiguration()

    }

    private fun setupView() {

        /*binding?.toolbar?.apply {

            show()

            showCloseSecondaryButton(imageConfiguration)
            {
                if (navigator.back(aboutYouNavController()).not())
                    navigator.back(rootNavController())

            }
        }*/

    }

    private fun applyConfiguration() {

        /*setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        imageView.setImageResource(imageConfiguration.logoStudySecondary())

        frameLayout.setBackgroundColor(configuration.theme.primaryColorStart.color())

        textView.text = configuration.text.profile.title
        textView.setTextColor(configuration.theme.secondaryColor.color())

        firstItem.applyData(
            configuration,
            requireContext().imageConfiguration.pregnancy(),
            configuration.text.profile.firstItem
        )

        firstItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.toAboutYouUserInfoPage(aboutYouNavController())
            }
        }

        firstItem.isVisible = user.customData.isNotEmpty()

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.devices(),
            configuration.text.profile.secondItem
        )

        secondItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.toAboutYouAppsAndDevicesPage(aboutYouNavController())
            }
        }

        thirdItem.applyData(
            configuration,
            requireContext().imageConfiguration.reviewConsent(),
            configuration.text.profile.thirdItem
        )

        thirdItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.toAboutYouReviewConsentPage(aboutYouNavController())
            }
        }

        thirdItem.isVisible =
            configuration.text.onboarding.sections.contains(ConsentStep.identifier)

        fourthItem.applyData(
            configuration,
            requireContext().imageConfiguration.permissions(),
            configuration.text.profile.fourthItem
        )

        fourthItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.toAboutYouPermissionsPage(aboutYouNavController())
            }
        }

        fifthItem.applyData(
            configuration,
            requireContext().imageConfiguration.dailySurveyTime(),
            configuration.text.profile.fifthItem
        )

        fifthItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.toAboutYouDailySurveyTimePage(aboutYouNavController())
            }
        }

        fifthItem.isVisible = configuration.text.profile.dailySurveyTimingHidden == 0

        disclaimer.text = configuration.text.profile.disclaimer*/
    }

}