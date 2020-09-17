package org.fouryouandme.studyinfo

import org.fouryouandme.core.entity.configuration.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.study_info.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.StudyInfo
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.*

class StudyInfoFragment : BaseFragment<StudyInfoViewModel>(R.layout.study_info) {

    override val viewModel: StudyInfoViewModel by lazy {

        viewModelFactory(this, getFactory { StudyInfoViewModel(navigator, IORuntime, injector.configurationModule()) })
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

        imageView.setImageResource(imageConfiguration.logoStudySecondary())

        frameLayout.setBackgroundColor(configuration.theme.primaryColorStart.color())

        textView.text = "Study Info"
        textView.setTextColor(configuration.theme.secondaryColor.color())

        contactInfoItem.applyData(
            configuration,
            requireContext().imageConfiguration.contactInfo(),
            configuration.text.studyInfo.contactInfo
        )

        contactInfoItem.setOnClickListener { Log.d("item clicked", "Contact Info clicked") }

        rewardsItem.applyData(
            configuration,
            requireContext().imageConfiguration.rewards(),
            configuration.text.studyInfo.rewards
        )

        rewardsItem.setOnClickListener { Log.d("item clicked", "Rewards clicked") }

        faqItem.applyData(
            configuration,
            requireContext().imageConfiguration.faq(),
            configuration.text.studyInfo.faq
        )
        faqItem.setOnClickListener { { Log.d("item clicked", "FAQ clicked") } }

    }
}