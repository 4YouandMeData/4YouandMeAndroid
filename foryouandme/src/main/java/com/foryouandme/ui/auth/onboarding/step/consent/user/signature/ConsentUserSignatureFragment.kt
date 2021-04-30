package com.foryouandme.ui.auth.onboarding.step.consent.user.signature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserError
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserLoading
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.ConsentUserEmailBinding
import com.foryouandme.databinding.ConsentUserSignatureBinding
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserAction
import com.github.gcacace.signaturepad.views.SignaturePad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserSignatureFragment : ConsentUserSectionFragment(
    R.layout.consent_user_signature
) {

    private val binding: ConsentUserSignatureBinding?
        get() = view?.let { ConsentUserSignatureBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    is ConsentUserLoading.UpdateUser ->
                        binding?.consentUserSignatureLoading?.setVisibility(it.active)
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    is ConsentUserError.UpdateUser -> errorToast(it.error, configuration)
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            setupView()
            applyConfiguration()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(ConsentUserAction.ConsentUserSignatureViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            consentUserFragment()
                .binding
                ?.toolbar
                ?.showBackSecondaryButton(imageConfiguration) { back() }

            viewBinding.action1.setOnClickListener {

                val signature = viewBinding.signaturePad.transparentSignatureBitmap
                if(signature != null)
                    viewModel.execute(ConsentUserAction.UpdateUser(signature))

            }

        }

    }

    private fun applyConfiguration() {

        val configuration = configuration
        val viewBinding = binding

        if (configuration != null && viewBinding != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.title.text = configuration.text.onboarding.user.signatureTitle
            viewBinding.title.setTextColor(configuration.theme.primaryTextColor.color())

            viewBinding.description.text = configuration.text.onboarding.user.signatureBody
            viewBinding.description.setTextColor(configuration.theme.primaryTextColor.color())

            viewBinding.signaturePlaceholder.isVisible = viewBinding.signaturePad.isEmpty
            viewBinding.signaturePlaceholder.text =
                configuration.text.onboarding.user.signaturePlaceholder
            viewBinding.signaturePlaceholder.setTextColor(
                configuration.theme.fourthTextColor.color()
            )

            viewBinding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {

                override fun onStartSigning() {
                    binding?.signaturePlaceholder?.isVisible = false
                    binding?.action1?.isEnabled = true
                }

                override fun onClear() {
                    binding?.signaturePlaceholder?.isVisible = true
                    binding?.action1?.isEnabled = false
                }

                override fun onSigned() {
                }

            })

            viewBinding.signatureLine.setBackgroundColor(
                configuration.theme.primaryTextColor.color()
            )

            viewBinding.clear.setImageResource(imageConfiguration.clear())
            viewBinding.clear.setOnClickListener { binding?.signaturePad?.clear() }

            viewBinding.clearText.text = configuration.text.onboarding.user.signatureClear
            viewBinding.clearText.setTextColor(configuration.theme.primaryTextColor.color())
            viewBinding.clearText.setOnClickListener { binding?.signaturePad?.clear() }

            viewBinding.shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    configuration.theme.primaryTextColor
                ).drawable(0.3f)

            viewBinding.action1.isEnabled = !viewBinding.signaturePad.isEnabled
            viewBinding.action1.background =
                button(
                    resources,
                    imageConfiguration.nextStepSecondary()
                )

        }
    }

}