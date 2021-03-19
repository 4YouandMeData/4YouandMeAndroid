package com.foryouandme.ui.auth.signin.pin

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
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
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
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
                    PinCodeLoading.Auth,
                    PinCodeLoading.Configuration ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    PinCodeError.Auth -> {
                        if(it.error is ForYouAndMeException.WrongCode)
                            setWrongCodeErrorVisibility(true)
                        else
                            errorToast(it.error, viewModel.state.configuration)
                    }
                    PinCodeError.Configuration ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(PinCodeStateEvent.GetConfiguration) }
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

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    PinCodeStateUpdate.Configuration -> applyConfiguration()
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        if (viewModel.state.configuration == null)
            viewModel.execute(PinCodeStateEvent.GetConfiguration)
        else
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
        val configuration = viewModel.state.configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            viewBinding.title.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.title.text = configuration.text.phoneVerification.title

            viewBinding.description.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.description.text = configuration.text.phoneVerification.body


            viewBinding.pinValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            viewBinding.pinValidation.setImageResource(
                if (isPinValid()) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            viewBinding.pin.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.pin.setHintTextColor(configuration.theme.secondaryColor.color())
            viewBinding.pin.backgroundTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
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

            viewBinding.line.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.checkbox.buttonTintList =
                checkbox(
                    configuration.theme.secondaryColor.color(),
                    configuration.theme.secondaryColor.color()
                )
            viewBinding.checkbox.isChecked = viewModel.state.legalCheckbox
            viewBinding.checkbox.jumpDrawablesToCurrentState()
            viewBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                binding?.action1?.isEnabled = isChecked && isPinValid()
                viewModel.execute(PinCodeStateEvent.SetLegalCheckbox(isChecked))
            }

            setLegalCheckboxText(
                viewBinding,
                configuration.theme,
                configuration.text.phoneVerification,
                configuration.text.url
            )

            viewBinding.action1.isEnabled = viewBinding.checkbox.isChecked && isPinValid()
            viewBinding.action1.background =
                button(resources, imageConfiguration.nextStep())

            viewBinding.wrongPinError.text =
                configuration.text.phoneVerification.error.errorMissingNumber
            viewBinding.wrongPinError.setTextColor(configuration.theme.primaryTextColor.color())
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
            SpanDroid()
                .append(
                    split.getOrElse(0) { "" },
                    spanList(requireContext()) { typeface(R.font.helvetica) }
                )
                .append(
                    if (privacyIndex > termsIndex) text.legalPrivacyPolicy
                    else text.legalTermsOfService,
                    spanList(requireContext()) {
                        click {

                            viewModel.execute(PinCodeStateEvent.LogPrivacyPolicy)
                            navigator.navigateTo(
                                rootNavController(),
                                AnywhereToWeb(
                                    if (privacyIndex > termsIndex) url.privacy
                                    else url.terms
                                )
                            )

                        }
                        typeface(R.font.helvetica_bold)
                        custom(ForegroundColorSpan(theme.secondaryColor.color()))
                        custom(UnderlineSpan())
                    }
                )
                .append(
                    split.getOrElse(1) { "" },
                    spanList(requireContext()) { typeface(R.font.helvetica) }
                )
                .append(
                    if (privacyIndex > termsIndex) text.legalTermsOfService
                    else text.legalPrivacyPolicy,
                    spanList(requireContext()) {
                        click {
                            viewModel.execute(PinCodeStateEvent.LogTermsOfService)
                            navigator.navigateTo(
                                rootNavController(),
                                AnywhereToWeb(
                                    if (privacyIndex > termsIndex) url.terms
                                    else url.privacy
                                )
                            )
                        }
                        typeface(R.font.helvetica_bold)
                        custom(ForegroundColorSpan(theme.secondaryColor.color()))
                        custom(UnderlineSpan())
                    }
                )
                .append(
                    split.getOrElse(2) { "" },
                    spanList(requireContext()) { typeface(R.font.helvetica) }
                )
                .toSpannableString()
    }

    private fun setWrongCodeErrorVisibility(
        visible: Boolean
    ) {

        val viewBinding = binding
        val configuration = viewModel.state.configuration

        if (viewBinding != null && configuration != null) {

            viewBinding.pin.setTextColor(
                if (visible) configuration.theme.primaryTextColor.color()
                else configuration.theme.secondaryColor.color()
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