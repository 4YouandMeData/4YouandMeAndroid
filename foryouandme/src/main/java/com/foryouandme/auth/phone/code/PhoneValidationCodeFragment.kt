package com.foryouandme.auth.phone.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.ext.*
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.phone_validation_code.*


class PhoneValidationCodeFragment : AuthSectionFragment<PhoneValidationCodeViewModel>(
    R.layout.phone_validation_code
) {

    private val args: PhoneValidationCodeFragmentArgs by navArgs()

    override val viewModel: PhoneValidationCodeViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                PhoneValidationCodeViewModel(
                    navigator,
                    injector.authModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    PhoneValidationCodeLoading.Auth -> loading.setVisibility(it.active)
                    PhoneValidationCodeLoading.ResendCode -> loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent { payload ->
                when (payload.cause) {
                    PhoneValidationCodeError.Auth -> {

                        when (payload.error) {

                            is ForYouAndMeError.WrongPhoneCode ->
                                configuration { setWrongCodeErrorVisibility(it, true) }
                            else ->
                                errorToast(payload.error)

                        }

                        startCoroutineAsync {
                            viewModel.back(authNavController(), rootNavController())
                        }

                    }
                    PhoneValidationCodeError.ResendCode ->
                        errorToast(payload.error)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            applyConfiguration(it)

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

            phone.setText(args.phone)

            action_1.setOnClickListenerAsync {
                viewModel.auth(
                    rootNavController(),
                    authNavController(),
                    ccp.fullNumberWithPlus,
                    code.text.toString()
                )
            }

            resend.setOnClickListenerAsync { viewModel.resendCode(ccp.fullNumberWithPlus) }
        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.activeColor.color())

            root.setBackgroundColor(configuration.theme.activeColor.color())

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.phoneVerification.codeTitle

            description.setTextColor(configuration.theme.secondaryColor.color())
            description.text = configuration.text.phoneVerification.codeBody

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

            ccp.setCcpClickable(false)
            ccp.setCountryForNameCode(args.countryCode)

            phone_description.text =
                configuration.text.phoneVerification.wrongNumber
            phone_description.setTextColor(configuration.theme.secondaryColor.color())

            phone_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            phone_validation.setImageResource(imageConfiguration.entryValid())

            phone.setTextColor(configuration.theme.secondaryColor.color())
            phone.setHintTextColor(configuration.theme.secondaryColor.color())
            phone.backgroundTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            phone.autoCloseKeyboard()

            phone.isEnabled = false

            change_phone.setOnClickListenerAsync {
                viewModel.back(authNavController(), rootNavController())
            }

            code_description.setTextColor(configuration.theme.secondaryColor.color())
            code_description.text =
                configuration.text.phoneVerification.codeDescription

            code_validation.setImageResource(
                if (code.length() == 6) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            code.setTextColor(configuration.theme.secondaryColor.color())
            code.setHintTextColor(configuration.theme.secondaryColor.color())
            code.backgroundTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            code.autoCloseKeyboard()
            code.addTextChangedListener { editable ->

                action_1.isEnabled = editable?.toString()?.length == 6

                configuration { setWrongCodeErrorVisibility(it, false) }

            }
            code.setOnFocusChangeListener { _, hasFocus ->

                code_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && code.length() == 6 -> imageConfiguration.entryValid()
                        else -> imageConfiguration.entryWrong()
                    }
                )
            }

            resend.setTextColor(configuration.theme.secondaryColor.color())
            resend.text =
                SpanDroid()
                    .append(
                        configuration.text.phoneVerification.resendCode,
                        spanList(requireContext()) {
                            click {}
                            typeface(R.font.helvetica)
                            custom(ForegroundColorSpan(configuration.theme.secondaryColor.color()))
                            custom(UnderlineSpan())
                        }
                    )
                    .toSpannableString()

            action_1.background =
                button(resources, imageConfiguration.nextStep())
            action_1.isEnabled = code.text?.toString()?.length == 6

            wrong_code_error.text =
                configuration.text.phoneVerification.error.errorWrongCode
            wrong_code_error.setTextColor(configuration.theme.primaryTextColor.color())
        }

    private suspend fun setWrongCodeErrorVisibility(
        configuration: Configuration,
        visible: Boolean
    ): Unit =
        evalOnMain {

            code.setTextColor(
                if (visible) configuration.theme.primaryTextColor.color()
                else configuration.theme.secondaryColor.color()
            )


            if (visible)
                wrong_code_error
                    .animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .start()
            else
                wrong_code_error
                    .animate()
                    .alpha(0f)
                    .setDuration(500L)
                    .start()

        }
}