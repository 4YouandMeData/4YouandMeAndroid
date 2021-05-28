package com.foryouandme.ui.auth.signin.phone

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
import com.foryouandme.databinding.EnterPhoneBinding
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
class EnterPhoneFragment : AuthSectionFragment(R.layout.enter_phone) {

    private val viewModel: EnterPhoneViewModel by viewModels()

    private val binding: EnterPhoneBinding?
        get() = view?.let { EnterPhoneBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    EnterPhoneLoading.PhoneNumberVerification ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    EnterPhoneError.PhoneNumberVerification -> {

                        when (it.error) {

                            is ForYouAndMeException.MissingPhoneNumber ->
                                setMissingPhoneErrorVisibility(true)
                            else ->
                                errorToast(it.error, configuration)

                        }
                    }
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is EnterPhoneToPhoneValidationCode ->
                        navigator.navigateTo(authNavController(), it)
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

        viewModel.execute(EnterPhoneStateEvent.ScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.toolbar.showBackButton(imageConfiguration) { back() }

            viewBinding.action1.setOnClickListener {

                hideKeyboard()
                binding?.let {
                    viewModel.execute(
                        EnterPhoneStateEvent.VerifyPhoneNumber(
                            it.ccp.fullNumberWithPlus,
                            it.phone.text.toString(),
                            it.ccp.selectedCountryNameCode
                        )
                    )
                }

            }

            val countryCode = viewModel.state.countryNameCode
            if (countryCode == null) {
                viewBinding.ccp.setAutoDetectedCountry(true)
                viewModel.execute(
                    EnterPhoneStateEvent.SetCountryCode(viewBinding.ccp.selectedCountryNameCode)
                )
            } else
                viewBinding.ccp.setCountryForNameCode(countryCode)

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

            viewBinding.ccp.setOnCountryChangeListener {
                binding?.ccp?.selectedCountryNameCode?.let {
                    viewModel.execute(EnterPhoneStateEvent.SetCountryCode(it))
                }
            }
            viewBinding.ccp.setCustomMasterCountries(
                config.countryCodes.fold(
                    "",
                    { acc, s ->
                        if (acc.isEmpty()) s
                        else "$acc,$s"
                    }
                )
            )
            viewBinding.ccp.contentColor = config.theme.secondaryColor.color()
            viewBinding.ccp.setFlagBorderColor(config.theme.secondaryColor.color())
            viewBinding.ccp.ccpDialogShowTitle = false
            viewBinding.ccp.enableDialogInitialScrollToSelection(true)
            viewBinding.ccp.setDialogBackgroundColor(config.theme.primaryColorStart.color())
            viewBinding.ccp.setDialogTextColor(config.theme.secondaryColor.color())
            viewBinding.ccp.setDialogSearchEditTextTintColor(config.theme.secondaryColor.color())
            viewBinding.ccp.setFastScrollerHandleColor(config.theme.secondaryColor.color())
            viewBinding.ccp.setTextSize(15f.spToPx(requireContext()))
            viewBinding.ccp.setDialogKeyboardAutoPopup(false)
            viewBinding.ccp.registerCarrierNumberEditText(viewBinding.phone)
            viewBinding.ccp.setNumberAutoFormattingEnabled(true)
            viewBinding.ccp.setPhoneNumberValidityChangeListener { isValid ->

                binding?.let {
                    it.action1.isEnabled = it.checkbox.isChecked && isValid
                }

            }

            viewBinding.phoneValidation.imageTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.phoneValidation.setImageResource(
                if (viewBinding.ccp.isValidFullNumber) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            viewBinding.phone.setTextColor(config.theme.secondaryColor.color())
            viewBinding.phone.setHintTextColor(config.theme.secondaryColor.color())
            viewBinding.phone.backgroundTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.phone.autoCloseKeyboard()
            viewBinding.phone.addTextChangedListener { setMissingPhoneErrorVisibility(false) }
            viewBinding.phone.setOnFocusChangeListener { _, hasFocus ->

                binding?.let {
                    it.phoneValidation.setImageResource(
                        when {
                            hasFocus ->
                                0
                            hasFocus.not() && it.ccp.isValidFullNumber.not() ->
                                imageConfiguration.entryWrong()
                            else ->
                                imageConfiguration.entryValid()
                        }
                    )
                }

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
                binding?.let {
                    it.action1.isEnabled = isChecked && it.ccp.isValidFullNumber
                    viewModel.execute(EnterPhoneStateEvent.SetLegalCheckbox(isChecked))
                }
            }

            setLegalCheckboxText(
                viewBinding,
                config.theme,
                config.text.phoneVerification,
                config.text.url
            )

            viewBinding.action1.isEnabled =
                viewBinding.checkbox.isChecked && viewBinding.ccp.isValidFullNumber
            viewBinding.action1.background =
                button(resources, imageConfiguration.nextStep())

            viewBinding.missingNumberError.text =
                config.text.phoneVerification.error.errorMissingNumber
            viewBinding.missingNumberError.setTextColor(config.theme.primaryTextColor.color())

        }

    }

    private fun setLegalCheckboxText(
        viewBinding: EnterPhoneBinding,
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
                    Span.Typeface(R.font.helvetica, requireContext())
                )
                .append(
                    if (privacyIndex > termsIndex) text.legalPrivacyPolicy
                    else text.legalTermsOfService,
                    Span.Click {

                        viewModel.execute(EnterPhoneStateEvent.LogPrivacyPolicy)
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

                        viewModel.execute(EnterPhoneStateEvent.LogPrivacyPolicy)
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
                    Span.Typeface(R.font.helvetica, requireContext()),
                )
                .toSpannableString()

    }

    private fun setMissingPhoneErrorVisibility(visible: Boolean) {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            viewBinding.phone.setTextColor(
                if (visible) config.theme.primaryTextColor.color()
                else config.theme.secondaryColor.color()
            )
            viewBinding.ccp.contentColor =
                if (visible) config.theme.primaryTextColor.color()
                else config.theme.secondaryColor.color()

            if (visible)
                viewBinding.missingNumberError
                    .animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .start()
            else
                viewBinding.missingNumberError
                    .animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .start()

        }

    }

}