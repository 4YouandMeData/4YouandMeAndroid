package org.fouryouandme.auth.wearable.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.firstOrNone
import arrow.core.toOption
import kotlinx.android.synthetic.main.wearable.*
import kotlinx.android.synthetic.main.wearable_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.wearable.WearableViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.wearable.Wearable
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackSecondaryButton

class WearablePageFragment : BaseFragment<WearableViewModel>(R.layout.wearable_page) {

    private val args: WearablePageFragmentArgs by navArgs()

    override val viewModel: WearableViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                WearableViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().wearable }
            .map { applyData(it.first, it.second) }
    }

    private fun setupView(): Unit {

        requireParentFragment().requireParentFragment().toolbar
            .toOption()
            .map {
                it.showBackSecondaryButton(imageConfiguration)
                { viewModel.back(findNavController()) }
            }
    }

    private fun applyData(configuration: Configuration, wearable: Wearable): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        wearable.pages.firstOrNone { it.id == args.id }
            .map { wearablePage ->
                page.applyData(
                    configuration,
                    wearablePage,
                    //Page("0", "test", "test", None, None, None, None, None, None, "https://admin-4youandme-staging.balzo.eu/users/wearable_oauth/oura".some(), None, None),
                    { viewModel.nextPage(findNavController(), it, false) },
                    { viewModel.handleSpecialLink(it) },
                    { viewModel.login(findNavController(), it, false) }
                )
            }

    }
}