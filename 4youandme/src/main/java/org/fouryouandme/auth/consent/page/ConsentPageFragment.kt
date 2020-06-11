package org.fouryouandme.auth.consent.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.extensions.list.foldable.firstOrNone
import kotlinx.android.synthetic.main.consent_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.ConsentViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackSecondaryButton

class ConsentPageFragment : BaseFragment<ConsentViewModel>(R.layout.consent_page) {

    private val args: ConsentPageFragmentArgs by navArgs()

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consent }
            .map { applyData(it.first, it.second) }

    }

    private fun setupView(): Unit {

        toolbar.showBackSecondaryButton(imageConfiguration)
        { viewModel.back(findNavController()) }

    }

    private fun applyData(configuration: Configuration, consent: Consent): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())


        consent.pages.firstOrNone { it.id == args.id }
            .map { data ->
                page.applyData(
                    configuration,
                    null,
                    false,
                    data
                )
                { option ->
                    option.fold(
                        {},
                        { viewModel.page(findNavController(), it.id, false) })
                }
            }

    }
}