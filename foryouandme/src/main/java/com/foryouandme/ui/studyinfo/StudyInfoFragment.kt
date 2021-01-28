package com.foryouandme.studyinfo

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.study_info.*

class StudyInfoFragment : BaseFragmentOld<StudyInfoViewModel>(R.layout.study_info) {

    override val viewModel: StudyInfoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                StudyInfoViewModel(
                    navigator,
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
                    is StudyInfoStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {
            if (viewModel.isInitialized().not()) {
                viewModel.initialize()
            }

            evalOnMain { applyConfiguration(viewModel.state().configuration) }
        }
    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        imageView.setImageResource(imageConfiguration.logoStudySecondary())

        frameLayout.setBackgroundColor(configuration.theme.primaryColorStart.color())

        textView.text = configuration.text.tab.studyInfoTitle
        textView.setTextColor(configuration.theme.secondaryColor.color())

        firstItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.aboutYou
        )

        firstItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.aboutYouPage(rootNavController())
            }
        }

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        secondItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.info(rootNavController())
            }
        }

        thirdItem.applyData(
            configuration,
            requireContext().imageConfiguration.rewards(),
            configuration.text.studyInfo.rewards
        )

        thirdItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.reward(rootNavController())
            }
        }

        fourthItem.applyData(
            configuration,
            requireContext().imageConfiguration.faq(),
            configuration.text.studyInfo.faq
        )

        fourthItem.setOnClickListener {
            startCoroutineAsync {
                viewModel.faq(rootNavController())
            }
        }

    }
}