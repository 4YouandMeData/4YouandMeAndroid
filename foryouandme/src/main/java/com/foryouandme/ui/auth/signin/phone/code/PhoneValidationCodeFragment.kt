package com.foryouandme.ui.auth.signin.phone.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.PhoneValidationCodeBinding
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.AuthSectionFragment
import com.giacomoparisi.spandroid.Span
import com.giacomoparisi.spandroid.SpanDroid
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PhoneValidationCodeFragment : AuthSectionFragment(R.layout.phone_validation_code) {

    private val args: PhoneValidationCodeFragmentArgs by navArgs()

    private val viewModel: PhoneValidationCodeViewModel by viewModels()

    private val binding: PhoneValidationCodeBinding?
        get() = view?.let { PhoneValidationCodeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    PhoneValidationCodeLoading.Auth,
                    PhoneValidationCodeLoading.ResendCode ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    PhoneValidationCodeError.Auth -> {

                        when (it.error) {

                            is ForYouAndMeException.WrongCode ->
                                setWrongCodeErrorVisibility(true)
                            else ->
                                errorToast(it.error, configuration)

                        }

                    }
                    PhoneValidationCodeError.ResendCode ->
                        errorToast(it.error, configuration)
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is PhoneValidationCodeToMain ->
                        navigator.navigateTo(rootNavController(), it)
                    is PhoneValidationCodeToOnboarding ->
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

        viewModel.execute(PhoneValidationStateEvent.LogScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.toolbar.showBackButton(imageConfiguration) { back() }

            viewBinding.phone.setText(args.phone)

            viewBinding.action1.setOnClickListener {

                hideKeyboard()
                binding?.let {
                    viewModel.execute(
                        PhoneValidationStateEvent.Auth(
                            it.ccp.fullNumberWithPlus,
                            it.code.text.toString(),
                            args.countryCode
                        )
                    )
                }
            }

            viewBinding.resend.setOnClickListener {
                binding?.ccp?.let {
                    viewModel.execute(PhoneValidationStateEvent.ResendCode(it.fullNumberWithPlus))
                }
            }

        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            setStatusBar(config.theme.activeColor.color())

            viewBinding.root.setBackgroundColor(config.theme.activeColor.color())

            viewBinding.title.setTextColor(config.theme.secondaryColor.color())
            viewBinding.title.text = config.text.phoneVerification.codeTitle

            viewBinding.description.setTextColor(config.theme.secondaryColor.color())
            viewBinding.description.text = config.text.phoneVerification.codeBody

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

            viewBinding.ccp.setCcpClickable(false)
            viewBinding.ccp.setCountryForNameCode(args.countryCode)

            viewBinding.phoneDescription.text =
                config.text.phoneVerification.wrongNumber
            viewBinding.phoneDescription.setTextColor(config.theme.secondaryColor.color())

            viewBinding.phoneValidation.imageTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.phoneValidation.setImageResource(imageConfiguration.entryValid())

            viewBinding.phone.setTextColor(config.theme.secondaryColor.color())
            viewBinding.phone.setHintTextColor(config.theme.secondaryColor.color())
            viewBinding.phone.backgroundTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.phone.autoCloseKeyboard()

            viewBinding.phone.isEnabled = false

            viewBinding.phoneLine.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.changePhone.setOnClickListener { back() }

            viewBinding.codeDescription.setTextColor(config.theme.secondaryColor.color())
            viewBinding.codeDescription.text =
                config.text.phoneVerification.codeDescription

            viewBinding.codeValidation.setImageResource(
                if (viewBinding.code.length() == 6) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            viewBinding.code.setTextColor(config.theme.secondaryColor.color())
            viewBinding.code.setHintTextColor(config.theme.secondaryColor.color())
            viewBinding.code.backgroundTintList =
                ColorStateList.valueOf(config.theme.secondaryColor.color())
            viewBinding.code.autoCloseKeyboard()
            viewBinding.code.addTextChangedListener { editable ->

                binding?.action1?.isEnabled = editable?.toString()?.length == 6
                setWrongCodeErrorVisibility(false)

            }
            viewBinding.code.setOnFocusChangeListener { _, hasFocus ->
                binding?.let {
                    it.codeValidation.setImageResource(
                        when {
                            hasFocus ->
                                0
                            hasFocus.not() && it.code.length() == 6 ->
                                imageConfiguration.entryValid()
                            else ->
                                imageConfiguration.entryWrong()
                        }
                    )
                }
            }

            viewBinding.codeLine.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.resend.setTextColor(config.theme.secondaryColor.color())
            viewBinding.resend.text =
                SpanDroid
                    .span()
                    .append(
                        config.text.phoneVerification.resendCode,
                        Span.Click {},
                        Span.Typeface(R.font.helvetica, requireContext()),
                        Span.Custom(ForegroundColorSpan(config.theme.secondaryColor.color())),
                        Span.Underline
                    )
                    .toSpannableString()

            viewBinding.action1.background =
                button(resources, imageConfiguration.nextStep())
            viewBinding.action1.isEnabled = viewBinding.code.text?.toString()?.length == 6

            viewBinding.wrongCodeError.text =
                config.text.phoneVerification.error.errorWrongCode
            viewBinding.wrongCodeError.setTextColor(config.theme.primaryTextColor.color())

        }

    }

    private fun setWrongCodeErrorVisibility(visible: Boolean) {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            viewBinding.code.setTextColor(
                if (visible) config.theme.primaryTextColor.color()
                else config.theme.secondaryColor.color()
            )

            if (visible)
                viewBinding.wrongCodeError
                    .animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .start()
            else
                viewBinding.wrongCodeError
                    .animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .start()

        }

    }

}