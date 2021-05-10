package com.foryouandme.ui.auth.signin.pin

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.EnterPinBinding
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.PhoneVerification
import com.foryouandme.entity.configuration.Theme
import com.foryouandme.entity.configuration.Url
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.configuration.checkbox.checkbox
import com.foryouandme.ui.auth.AuthSectionFragment
import com.giacomoparisi.spandroid.Span
import com.giacomoparisi.spandroid.SpanDroid
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PinCodeFragment : AuthSectionFragment(R.layout.enter_pin) {

    private val viewModel: PinCodeViewModel by viewModels()

    private val binding: EnterPinBinding?
        get() = view?.let { EnterPinBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    PinCodeLoading.Auth ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    PinCodeError.Auth -> {
                        if (it.error is ForYouAndMeException.WrongCode)
                            setWrongCodeErrorVisibility(true)
                        else
                            errorToast(it.error, configuration)
                    }
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    PinCodeToOnboarding -> navigator.navigateTo(authNavController(), it)
                    PinCodeToMain -> navigator.navigateTo(rootNavController(), it)
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

        viewModel.execute(PinCodeStateEvent.ScreenViewed)

    }

    private fun setupView() {

        val videoBinding = binding

        if (videoBinding != null) {

            videoBinding.toolbar.showBackButton(imageConfiguration) { back() }

            videoBinding.action1.setOnClickListenerAsync {

                hideKeyboardSuspend()
                binding?.pin
                    ?.text
                    ?.toString()
                    ?.let { viewModel.execute(PinCodeStateEvent.Auth(it)) }

            }

        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            setStatusBar(config.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    config.theme.primaryColorStart,
                    config.theme.primaryColorEnd
                ).drawable()

            viewBinding.title.setTextColor(config.theme.secondaryColor.color())
            viewBinding.title.text = config.text.phoneVerification.title

            viewBinding.description.setTextColor(config.theme.secondaryColor.color())
            viewBinding.description.text = config.text.phoneVerification.body


            viewBinding.pinValidation.imageTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.pinValidation.setImageResource(
                if (isPinValid()) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            viewBinding.pin.setTextColor(config.theme.secondaryColor.color())
            viewBinding.pin.setHintTextColor(config.theme.secondaryColor.color())
            viewBinding.pin.backgroundTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.pin.autoCloseKeyboard()
            viewBinding.pin.addTextChangedListener {
                viewBinding.action1.isEnabled = viewBinding.checkbox.isChecked && isPinValid()
                setWrongCodeErrorVisibility(false)
            }
            viewBinding.pin.setOnFocusChangeListener { _, hasFocus ->
                binding?.pinValidation?.setImageResource(
                    when {
                        hasFocus ->
                            0
                        hasFocus.not() && isPinValid().not() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            viewBinding.line.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.checkbox.buttonTintList =
                checkbox(
                    config.theme.secondaryColor.color(),
                    config.theme.secondaryColor.color()
                )
            viewBinding.checkbox.isChecked = viewModel.state.legalCheckbox
            viewBinding.checkbox.jumpDrawablesToCurrentState()
            viewBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                binding?.action1?.isEnabled = isChecked && isPinValid()
                viewModel.execute(PinCodeStateEvent.SetLegalCheckbox(isChecked))
            }

            setLegalCheckboxText(
                viewBinding,
                config.theme,
                config.text.phoneVerification,
                config.text.url
            )

            viewBinding.action1.isEnabled = viewBinding.checkbox.isChecked && isPinValid()
            viewBinding.action1.background =
                button(resources, imageConfiguration.nextStep())

            viewBinding.wrongPinError.text =
                config.text.phoneVerification.error.errorMissingNumber
            viewBinding.wrongPinError.setTextColor(config.theme.primaryTextColor.color())
        }

    }

    private fun setLegalCheckboxText(
        viewBinding: EnterPinBinding,
        theme: Theme,
        text: PhoneVerification,
        url: Url
    ) {

        viewBinding.checkboxText.setTextColor(theme.secondaryColor.color())
        viewBinding.checkboxText.movementMethod = LinkMovementMethod.getInstance()

        val privacyIndex = text.legal.indexOf(text.legalPrivacyPolicy)
        val termsIndex = text.legal.indexOf(text.legalTermsOfService)

        val privacySplit = text.legal.split(text.legalPrivacyPolicy)
        val split = privacySplit.flatMap { it.split(text.legalTermsOfService) }

        viewBinding.checkboxText.text =
            SpanDroid
                .span()
                .append(
                    split.getOrElse(0) { "" },
                    Span.Typeface(R.font.helvetica, requireContext()),
                )
                .append(
                    if (privacyIndex > termsIndex) text.legalPrivacyPolicy
                    else text.legalTermsOfService,
                    Span.Click {

                        viewModel.execute(PinCodeStateEvent.LogPrivacyPolicy)
                        navigator.navigateTo(
                            rootNavController(),
                            AnywhereToWeb(
                                if (privacyIndex > termsIndex) url.privacy
                                else url.terms
                            )
                        )

                    },
                    Span.Typeface(R.font.helvetica, requireContext()),
                    Span.Custom(ForegroundColorSpan(theme.secondaryColor.color())),
                    Span.Underline
                )
                .append(
                    split.getOrElse(1) { "" },
                    Span.Typeface(R.font.helvetica, requireContext())
                )
                .append(
                    if (privacyIndex > termsIndex) text.legalTermsOfService
                    else text.legalPrivacyPolicy,
                    Span.Click {
                        viewModel.execute(PinCodeStateEvent.LogTermsOfService)
                        navigator.navigateTo(
                            rootNavController(),
                            AnywhereToWeb(
                                if (privacyIndex > termsIndex) url.terms
                                else url.privacy
                            )
                        )
                    },
                    Span.Typeface(R.font.helvetica, requireContext()),
                    Span.Custom(ForegroundColorSpan(theme.secondaryColor.color())),
                    Span.Underline
                )
                .append(
                    split.getOrElse(2) { "" },
                    Span.Typeface(R.font.helvetica, requireContext())
                )
                .toSpannableString()
    }

    private fun setWrongCodeErrorVisibility(visible: Boolean) {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            viewBinding.pin.setTextColor(
                if (visible) config.theme.primaryTextColor.color()
                else config.theme.secondaryColor.color()
            )

            if (visible)
                viewBinding.wrongPinError
                    .animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .start()
            else
                viewBinding.wrongPinError
                    .animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .start()

        }

    }

    private fun isPinValid(): Boolean {

        val pin = binding?.pin?.text?.toString()
        return pin != null && pin.isBlank().not() && pin.isEmpty().not()

    }

}