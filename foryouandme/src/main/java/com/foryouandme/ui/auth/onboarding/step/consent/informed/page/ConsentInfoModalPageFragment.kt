package com.foryouandme.ui.auth.onboarding.step.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.show
import com.foryouandme.core.ext.showCloseButton
import com.foryouandme.databinding.ConsentInfoModalPageBinding
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoAbort
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoModalPageFragment : ConsentInfoSectionFragment(R.layout.consent_info_modal_page) {

    private val args: ConsentInfoPageFragmentArgs by navArgs()

    private val binding: ConsentInfoModalPageBinding?
        get() = view?.let { ConsentInfoModalPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ConsentInfoStateUpdate.ConsentInfo -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setUpView() {

        consentInfoFragment().binding?.toolbar?.apply {

            show()
            showCloseButton(imageConfiguration) { back() }

        }

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val consentInfo = viewModel.state.consentInfo

        if (viewBinding != null && configuration != null && consentInfo != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            consentInfoFragment()
                .showAbort(
                    configuration.theme.primaryColorEnd.color(),
                    ConsentInfoAbort.FromPage(args.id)
                )

            consentInfo.pages.firstOrNull { it.id == args.id }
                ?.let { data ->
                    viewBinding.title.setHtmlText(data.title, true)
                    viewBinding.description.setHtmlText(data.body, true)
                }

        }
    }

}