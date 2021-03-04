package com.foryouandme.ui.studyinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.StudyInfoBinding
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.main.MainPageToAboutYouPage
import com.foryouandme.ui.main.MainPageToFaq
import com.foryouandme.ui.main.MainPageToInformation
import com.foryouandme.ui.main.MainPageToReward
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StudyInfoFragment : BaseFragment(R.layout.study_info) {

    private val viewModel: StudyInfoViewModel by viewModels()

    private val binding: StudyInfoBinding?
        get() = view?.let { StudyInfoBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is StudyInfoStateUpdate.Initialization ->
                        applyConfiguration(it.configuration)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {

                binding?.loading?.setVisibility(false)

                when (it.cause) {
                    StudyInfoError.Initialization ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(StudyInfoStateEvent.Initialization) }
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    StudyInfoLoading.Initialization ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val configuration = viewModel.state.configuration

        if (configuration == null)
            viewModel.execute(StudyInfoStateEvent.Initialization)
        else
            applyConfiguration(configuration)

    }

    private fun applyConfiguration(configuration: Configuration) {

        val viewBinding = binding

        setStatusBar(configuration.theme.primaryColorStart.color())

        viewBinding?.imageView?.setImageResource(imageConfiguration.logoStudySecondary())

        viewBinding?.frameLayout?.setBackgroundColor(configuration.theme.primaryColorStart.color())

        viewBinding?.textView?.text = configuration.text.tab.studyInfoTitle
        viewBinding?.textView?.setTextColor(configuration.theme.secondaryColor.color())

        viewBinding?.firstItem?.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.aboutYou
        )

        viewBinding?.firstItem?.setOnClickListener {
            navigator.navigateTo(rootNavController(), MainPageToAboutYouPage)
        }

        viewBinding?.secondItem?.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        viewBinding?.secondItem?.setOnClickListener {
            navigator.navigateTo(rootNavController(), MainPageToInformation)
        }

        viewBinding?.thirdItem?.applyData(
            configuration,
            requireContext().imageConfiguration.rewards(),
            configuration.text.studyInfo.rewards
        )

        viewBinding?.thirdItem?.setOnClickListener {
            navigator.navigateTo(rootNavController(), MainPageToReward)
        }

        viewBinding?.fourthItem?.applyData(
            configuration,
            requireContext().imageConfiguration.faq(),
            configuration.text.studyInfo.faq
        )

        viewBinding?.fourthItem?.setOnClickListener {
            navigator.navigateTo(rootNavController(), MainPageToFaq)
        }

    }
}