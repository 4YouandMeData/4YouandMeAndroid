package org.fouryouandme.auth.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.extensions.list.foldable.firstOrNone
import arrow.core.getOrElse
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_modal_page.*
import kotlinx.android.synthetic.main.consent_info_page.*
import kotlinx.android.synthetic.main.consent_info_page.root
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoFragment
import org.fouryouandme.auth.consent.informed.ConsentInfoViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

class ConsentInfoModalPageFragment : BaseFragment<ConsentInfoViewModel>(R.layout.consent_info_modal_page) {

    private val args: ConsentInfoPageFragmentArgs by navArgs()

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consentInfo }
            .map { applyData(it.first, it.second) }

    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {

                it.show()

                it.showCloseButton(imageConfiguration)
                { viewModel.back(findNavController()) }

            }

    }

    private fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        (requireParentFragment().requireParentFragment() as? ConsentInfoFragment)
            .toOption()
            .map { it.showAbort(configuration, configuration.theme.primaryColorEnd.color()) }

        consentInfo.pages.firstOrNone { it.id == args.id }
            .map { data ->
                title.text = data.title
                description.text = data.body
            }

    }
}