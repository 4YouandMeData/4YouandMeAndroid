package org.fouryouandme.aboutyou.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.toOption
import kotlinx.android.synthetic.main.about_you_menu.*
import kotlinx.android.synthetic.main.about_you_menu.root
import org.fouryouandme.R
import org.fouryouandme.aboutyou.*
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouMenuFragment : AboutYouSectionFragment(R.layout.about_you_menu) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is AboutYouStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        startCoroutineAsync {
            if (viewModel.isInitialized().not()) {
                viewModel.initialize()
            }

            evalOnMain { applyConfiguration(viewModel.state().configuration) }
        }
    }

    private fun setupView(): Unit {
        requireParentFragment()
            .toolbar
            .toOption()
            .map {
                it.show()

                it.showCloseSecondaryButton(imageConfiguration)
                {
                    startCoroutineAsync {
                        viewModel.back(aboutYouMenuNavController(), aboutYouNavController())
                    }
                }
            }
    }

    private fun applyConfiguration(configuration: Configuration) {

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

        firstItem.setOnClickListener { Log.d("item clicked", "Your Pregnancy clicked") }

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.devices(),
            configuration.text.profile.secondItem
        )

        secondItem.setOnClickListener { Log.d("item clicked", "Your Apps clicked") }

        thirdItem.applyData(
            configuration,
            requireContext().imageConfiguration.reviewConsent(),
            configuration.text.profile.thirdItem
        )

        thirdItem.setOnClickListener { Log.d("item clicked", "Review Consent clicked") }

        fourthItem.applyData(
            configuration,
            requireContext().imageConfiguration.permissions(),
            configuration.text.profile.fourthItem
        )

        fourthItem.setOnClickListener { Log.d("item clicked", "Permissions clicked") }

        disclaimer.text = configuration.text.profile.disclaimer
    }

    fun aboutYouMenuNavController() = AboutYouMenuNavController(findNavController())
}