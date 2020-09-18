package org.fouryouandme.aboutyou.page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.toOption
import kotlinx.android.synthetic.main.about_you.*
import kotlinx.android.synthetic.main.about_you.root
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouFragment : BaseFragment<AboutYouViewModel>(R.layout.about_you) {
    override val viewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                AboutYouViewModel(
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
                { viewModel.back(findNavController()) }
            }
    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        imageView.setImageResource(imageConfiguration.logoStudySecondary())

        frameLayout.setBackgroundColor(configuration.theme.primaryColorStart.color())

        textView.text = "About you"
        textView.setTextColor(configuration.theme.secondaryColor.color())

        firstItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        firstItem.setOnClickListener { Log.d("item clicked", "Your Pregnancy clicked") }

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        secondItem.setOnClickListener { Log.d("item clicked", "Your Apps clicked") }

        thirdItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        thirdItem.setOnClickListener { Log.d("item clicked", "Review Consent clicked") }

        fourthItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        fourthItem.setOnClickListener { Log.d("item clicked", "Permissions clicked") }
    }

}