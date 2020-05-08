package org.fouryouandme.auth.phone

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.enter_phone.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackButton

class EnterPhoneFragment : BaseFragment<EnterPhoneViewModel>(R.layout.enter_phone) {

    override val viewModel: EnterPhoneViewModel by lazy {
        viewModelFactory(this, getFactory { EnterPhoneViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is EnterPhoneStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }

        viewModel.initialize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration.map { applyConfiguration(it) }
        setupView()
    }

    private fun setupView() {

        logo.setImageResource(imageConfiguration.logo())

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.phoneVerificationTitle

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.text = configuration.text.phoneVerificationBody

    }
}