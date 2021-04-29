package com.foryouandme.ui.auth.onboarding.step.consent.user.email.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.color
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackButton
import com.foryouandme.databinding.ConsentUserEmailValidationCodeBinding
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserAction
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserError
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserLoading
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.giacomoparisi.spandroid.Span
import com.giacomoparisi.spandroid.SpanDroid
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserEmailValidationCodeFragment : ConsentUserSectionFragment(
    R.layout.consent_user_email_validation_code
) {

    private val binding: ConsentUserEmailValidationCodeBinding?
        get() = view?.let { ConsentUserEmailValidationCodeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    ConsentUserLoading.ConfirmEmail ->
                        binding?.consentUserEmailLoading?.setVisibility(it.active)
                    ConsentUserLoading.ResendConfirmationEmail ->
                        binding?.consentUserEmailLoading?.setVisibility(it.active)
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ConsentUserError.ConfirmEmail ->
                        errorToast(it.error, configuration)
                    ConsentUserError.ResendConfirmationEmail ->
                        errorToast(it.error, configuration)
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

        viewModel.execute(ConsentUserAction.ConsentEmailValidationViewed)

    }

    private fun setupView() {

        consentUserFragment()
            .binding
            ?.toolbar
            ?.showBackButton(imageConfiguration) { back() }

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.action1.background = button(resources, imageConfiguration.nextStep())
            viewBinding.action1.setOnClickListener {
                viewModel.execute(
                    ConsentUserAction.ConfirmEmail(
                        binding?.code?.text.toString().trim()
                    )
                )
            }

            viewBinding.resend.setOnClickListener {
                viewModel.execute(ConsentUserAction.ResendEmail)
            }
        }
    }

    private fun applyConfiguration() {

        val configuration = configuration
        val viewBinding = binding

        if (configuration != null && viewBinding != null) {

            setStatusBar(configuration.theme.activeColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.activeColor.color())

            viewBinding.title.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.title.text = configuration.text.onboarding.user.emailVerificationTitle

            viewBinding.description.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.description.text = configuration.text.onboarding.user.emailVerificationBody

            viewBinding.email.text = configuration.text.onboarding.user.emailVerificationWrongMail
            viewBinding.email.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.emailEntry.setBackgroundColor(color(android.R.color.transparent))
            viewBinding.emailEntry.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.emailEntry.setText(viewModel.state.email)
            viewBinding.email.isEnabled = false

            viewBinding.emailValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            viewBinding.emailValidation.setImageResource(imageConfiguration.entryValid())

            viewBinding.emailLine.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.changeEmail.setOnClickListener { back() }

            viewBinding.code.text =
                configuration.text.onboarding.user.emailVerificationCodeDescription
            viewBinding.code.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.codeEntry.setBackgroundColor(color(android.R.color.transparent))
            viewBinding.codeEntry.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.codeEntry.addTextChangedListener {
                binding?.action1?.isEnabled = it.toString().trim().length == 6
            }
            viewBinding.codeEntry.setOnFocusChangeListener { _, hasFocus ->

                binding?.codeValidation?.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && binding?.codeEntry?.text.toString().trim().length != 6 ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            viewBinding.codeValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            viewBinding.codeValidation.setImageResource(
                if (viewBinding.codeEntry.text.toString().length == 6)
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            viewBinding.codeLine.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.resend.text =
                SpanDroid.span()
                    .append(
                        configuration.text.onboarding.user.emailVerificationResend,
                        Span.Click {},
                        Span.Typeface(R.font.helvetica, requireContext()),
                        Span.Custom(
                            ForegroundColorSpan(configuration.theme.secondaryColor.color())
                        ),
                        Span.Underline
                    ).toSpannableString()

            viewBinding.action1.isEnabled =
                viewBinding.codeEntry.text.toString().trim().length == 6

        }
    }

}