package org.fouryouandme.studyinfo

import org.fouryouandme.core.entity.configuration.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
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

        viewModelFactory(
            this,
            getFactory {
                StudyInfoViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            })
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
            configuration.text.studyInfo.contactInfo
        )

        firstItem.setOnClickListener {
            viewModel.aboutYouPage(rootNavController())
        }

        secondItem.applyData(
            configuration,
            requireContext().imageConfiguration.rewards(),
            configuration.text.studyInfo.rewards
        )

        secondItem.setOnClickListener { Log.d("item clicked", "Rewards clicked") }

        thirdItem.applyData(
            configuration,
            requireContext().imageConfiguration.faq(),
            configuration.text.studyInfo.faq
        )

        thirdItem.setOnClickListener { { Log.d("item clicked", "FAQ clicked") } }

    }
}