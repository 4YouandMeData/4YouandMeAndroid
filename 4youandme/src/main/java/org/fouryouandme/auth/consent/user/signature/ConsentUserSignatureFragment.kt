package org.fouryouandme.auth.consent.user.signature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_signature.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackButton

class ConsentUserSignatureFragment : BaseFragment<ConsentUserViewModel>(
    R.layout.consent_user_signature
) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentUserViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { viewModel.state().configuration.bind() }
            .map { applyConfiguration(it) }
    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {
                it.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
            }

        next.setOnClickListener {
            viewModel.updateUser(rootNavController(), signature_pad.signatureBitmap)
        }

    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        title.text = configuration.text.onboarding.user.signatureTitle
        title.setTextColor(configuration.theme.primaryTextColor.color())

        description.text = configuration.text.onboarding.user.signatureBody
        description.setTextColor(configuration.theme.primaryTextColor.color())

        signature_placeholder.isVisible = signature_pad.isEmpty
        signature_placeholder.text = configuration.text.onboarding.user.signaturePlaceholder
        signature_placeholder.setTextColor(configuration.theme.fourthTextColor.color())

        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {

            override fun onStartSigning() {
                signature_placeholder.isVisible = false
                next.isEnabled = true
            }

            override fun onClear() {
                signature_placeholder.isVisible = true
                next.isEnabled = false
            }

            override fun onSigned() {
            }

        })

        signature_line.setBackgroundColor(configuration.theme.primaryTextColor.color())

        clear.setImageResource(imageConfiguration.clear())
        clear.setOnClickListener { signature_pad.clear() }

        clear_text.text = configuration.text.onboarding.user.signatureClear
        clear_text.setTextColor(configuration.theme.primaryTextColor.color())
        clear_text.setOnClickListener { signature_pad.clear() }

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryTextColor
            ).drawable(0.3f)

        next.isEnabled = !signature_pad.isEnabled
        next.background =
            button(
                resources,
                imageConfiguration.signUpNextStepSecondary()

            )

    }

}