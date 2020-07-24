package org.fouryouandme.auth.wearable.success

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
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
import org.fouryouandme.core.ext.*

class WearableSuccessFragment : BaseFragment<WearableViewModel>(R.layout.wearable_page) {

    override val viewModel: WearableViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { WearableViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().wearable }
            .map { applyData(it.first, it.second) }

        setupView()

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

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration,
            wearable.successPage,
            { viewModel.openMain(rootNavController()) },
            { },
            { _, _ -> }
        )

    }

}