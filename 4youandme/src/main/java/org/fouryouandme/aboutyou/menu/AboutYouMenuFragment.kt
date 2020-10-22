package org.fouryouandme.aboutyou.menu

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.about_you_menu.*
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.*

class AboutYouMenuFragment :
    AboutYouSectionFragment<AboutYouMenuViewModel>(R.layout.about_you_menu) {
    override val viewModel: AboutYouMenuViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouMenuViewModel(
                    navigator,
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

    private suspend fun setupView(): Unit =
        evalOnMain {

            toolbar
                .apply {

                    show()

                    showCloseSecondaryButton(imageConfiguration)
                    {
                        startCoroutineAsync {
                            aboutYouViewModel.back(aboutYouNavController(), rootNavController())
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

            disclaimer.text = configuration.text.profile.disclaimer
        }

}