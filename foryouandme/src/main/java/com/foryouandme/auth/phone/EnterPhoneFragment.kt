package com.foryouandme.auth.phone

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.configuration.*
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.configuration.checkbox.checkbox
import com.foryouandme.core.ext.*
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.enter_phone.*


class EnterPhoneFragment : AuthSectionFragment<EnterPhoneViewModel>(R.layout.enter_phone) {

    override val viewModel: EnterPhoneViewModel by lazy {
        viewModelFactory(this, getFactory {
            EnterPhoneViewModel(
                navigator,
                injector.authModule(),
                injector.analyticsModule()
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    EnterPhoneLoading.PhoneNumberVerification -> loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent { payload ->
                when (payload.cause) {
                    EnterPhoneError.PhoneNumberVerification -> {

                        when (payload.error) {

                            is ForYouAndMeError.MissingPhoneNumber ->
                                configuration { setMissingPhoneErrorVisibility(it, true) }
                            else ->
                                startCoroutineAsync { viewModel.toastError(payload.error) }
                        }
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            applyConfiguration(it)

            viewModel.state().countryNameCode.foldSuspend(
                {
                    evalOnMain { ccp.setAutoDetectedCountry(true) }
                    viewModel.setCountryNameCode(ccp.selectedCountryNameCode)
                },
                { evalOnMain { ccp.setCountryForNameCode(it) } }
            )

            setupView()
        }
    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logScreenViewed() }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            toolbar.showBackButton(imageConfiguration) {
                startCoroutineAsync { viewModel.back(authNavController(), rootNavController()) }
            }

            action_1.setOnClickListenerAsync {

                viewModel.verifyNumber(
                    authNavController(),
                    ccp.fullNumberWithPlus,
                    phone.text.toString(),
                    ccp.selectedCountryNameCode
                )

            }
        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.phoneVerification.title

            description.setTextColor(configuration.theme.secondaryColor.color())
            description.text = configuration.text.phoneVerification.body

            ccp.setOnCountryChangeListener {
                startCoroutineAsync { viewModel.setCountryNameCode(ccp.selectedCountryNameCode) }
            }
            ccp.setCustomMasterCountries(
                configuration.countryCodes.fold(
                    "",
                    { acc, s ->
                        if (acc.isEmpty()) s
                        else "$acc,$s"
                    }
                )
            )
            ccp.contentColor = configuration.theme.secondaryColor.color()
            ccp.setFlagBorderColor(configuration.theme.secondaryColor.color())
            ccp.ccpDialogShowTitle = false
            ccp.enableDialogInitialScrollToSelection(true)
            ccp.setDialogBackgroundColor(configuration.theme.primaryColorStart.color())
            ccp.setDialogTextColor(configuration.theme.secondaryColor.color())
            ccp.setDialogSearchEditTextTintColor(configuration.theme.secondaryColor.color())
            ccp.setFastScrollerHandleColor(configuration.theme.secondaryColor.color())
            ccp.setTextSize(15f.spToPx(requireContext()))
            ccp.setDialogKeyboardAutoPopup(false)
            ccp.registerCarrierNumberEditText(phone)
            ccp.setNumberAutoFormattingEnabled(true)
            ccp.setPhoneNumberValidityChangeListener {

                action_1.isEnabled = checkbox.isChecked && it

            }

            phone_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            phone_validation.setImageResource(
                if (ccp.isValidFullNumber) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            phone.setTextColor(configuration.theme.secondaryColor.color())
            phone.setHintTextColor(configuration.theme.secondaryColor.color())
            phone.backgroundTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            phone.autoCloseKeyboard()
            phone.addTextChangedListener {
                configuration { setMissingPhoneErrorVisibility(it, false) }
            }
            phone.setOnFocusChangeListener { _, hasFocus ->
                phone_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && ccp.isValidFullNumber.not() -> imageConfiguration.entryWrong()
                        else -> imageConfiguration.entryValid()
                    }
                )
            }

            checkbox.buttonTintList =
                checkbox(
                    configuration.theme.secondaryColor.color(),
                    configuration.theme.secondaryColor.color()
                )
            checkbox.isChecked = viewModel.state().legalCheckbox
            checkbox.jumpDrawablesToCurrentState()
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                action_1.isEnabled = isChecked && ccp.isValidFullNumber
                startCoroutineAsync { viewModel.setLegalCheckbox(isChecked) }
            }

            setLegalCheckboxText(
                configuration.theme,
                configuration.text.phoneVerification,
                configuration.text.url
            )

            action_1.isEnabled = checkbox.isChecked && ccp.isValidFullNumber
            action_1.background =
                button(resources, imageConfiguration.nextStep())

            missing_number_error.text =
                configuration.text.phoneVerification.error.errorMissingNumber
            missing_number_error.setTextColor(configuration.theme.primaryTextColor.color())
        }

    private suspend fun setLegalCheckboxText(theme: Theme, text: PhoneVerification, url: Url) =
        evalOnMain {

            checkbox_text.setTextColor(theme.secondaryColor.color())
            checkbox_text.movementMethod = LinkMovementMethod.getInstance()

            val privacyIndex = text.legal.indexOf(text.legalPrivacyPolicy)
            val termsIndex = text.legal.indexOf(text.legalTermsOfService)

            val privacySplit = text.legal.split(text.legalPrivacyPolicy)
            val split = privacySplit.flatMap { it.split(text.legalTermsOfService) }

            checkbox_text.text =
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

                                startCoroutineAsync {

                                    viewModel.logPrivacyPolicy()

                                    viewModel.web(
                                        rootNavController(),
                                        if (privacyIndex > termsIndex) url.privacy
                                        else url.terms
                                    )

                                }

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
                                startCoroutineAsync {

                                    viewModel.logTermsOfService()

                                    viewModel.web(
                                        rootNavController(),
                                        if (privacyIndex > termsIndex) url.terms
                                        else url.privacy
                                    )

                                }
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

    private suspend fun setMissingPhoneErrorVisibility(
        configuration: Configuration,
        visible: Boolean
    ): Unit =
        evalOnMain {

            phone.setTextColor(
                if (visible) configuration.theme.primaryTextColor.color()
                else configuration.theme.secondaryColor.color()
            )
            ccp.contentColor =
                if (visible) configuration.theme.primaryTextColor.color()
                else configuration.theme.secondaryColor.color()

            if (visible)
                missing_number_error
                    .animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .start()
            else
                missing_number_error
                    .animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .start()

        }
}