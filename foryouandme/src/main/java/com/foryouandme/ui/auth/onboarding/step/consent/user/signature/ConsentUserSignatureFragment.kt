package com.foryouandme.ui.auth.onboarding.step.consent.user.signature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserError
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserLoading
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.*
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_signature.*

class ConsentUserSignatureFragment : ConsentUserSectionFragment(
    R.layout.consent_user_signature
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    is ConsentUserLoading.UpdateUser ->
                        consent_user_signature_loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    is ConsentUserError.UpdateUser ->
                        startCoroutineAsync { viewModel.toastError(it.error) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentUserAndConfiguration { config, _ ->

            setupView()
            applyConfiguration(config)

        }

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logConsentSignatureScreenViewed() }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            consentUserFragment()
                .toolbar
                .showBackSecondaryButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            consentUserNavController(),
                            consentNavController(),
                            onboardingStepNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }

            action_1.setOnClickListener {

                startCoroutineAsync {
                    viewModel.updateUser(
                        rootNavController(),
                        consentUserNavController(),
                        signature_pad.transparentSignatureBitmap
                    )
                }

            }

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

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
                    action_1.isEnabled = true
                }

                override fun onClear() {
                    signature_placeholder.isVisible = true
                    action_1.isEnabled = false
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

            action_1.isEnabled = !signature_pad.isEnabled
            action_1.background =
                button(
                    resources,
                    imageConfiguration.nextStepSecondary()

                )

        }

}