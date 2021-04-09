package com.foryouandme.ui.aboutyou.menu

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.*
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentStep
import kotlinx.android.synthetic.main.about_you_menu.*

class AboutYouMenuFragment :
    AboutYouSectionFragment<AboutYouMenuViewModel>(R.layout.about_you_menu) {
    override val viewModel: AboutYouMenuViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouMenuViewModel(
                    navigator,
                    injector.analyticsModule()
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAndConfiguration { config, user ->
            setupView()
            applyConfiguration(config, user)
        }
    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logScreenViewed() }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            toolbar
                .apply {

                    show()

                    showCloseSecondaryButton(imageConfiguration)
                    {
                        startCoroutineAsync {
                            evalOnMain {
                                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
                            }
                        }
                    }
                }

        }

    private suspend fun applyConfiguration(configuration: Configuration, user: User): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

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

            fifthItem.isVisible = true // TODO: set visibility toggle logic

            disclaimer.text = configuration.text.profile.disclaimer
        }

}