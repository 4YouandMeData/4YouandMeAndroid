package org.fouryouandme.auth.phone

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.enter_phone.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.configuration.*
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.configuration.checkbox.checkbox
import org.fouryouandme.core.ext.*


class EnterPhoneFragment : BaseFragment<EnterPhoneViewModel>(R.layout.enter_phone) {

    override val viewModel: EnterPhoneViewModel by lazy {
        viewModelFactory(this, getFactory { EnterPhoneViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is EnterPhoneStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    EnterPhoneLoading.PhoneNumberVerification -> loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    EnterPhoneError.PhoneNumberVerification -> {

                        when (it.error) {

                            is FourYouAndMeError.MissingPhoneNumber ->
                                setMissingPhoneErrorVisibility(true)
                            else ->
                                viewModel.toastError(it.error)
                        }
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration
            .fold(
                { viewModel.initialize() },
                { applyConfiguration(it) }
            )

        viewModel.state().countryNameCode.fold(
            {
                ccp.setAutoDetectedCountry(true)
                viewModel.setCountryNameCode(ccp.selectedCountryNameCode)
            },
            { ccp.setCountryForNameCode(it) }
        )

        setupView()
    }

    private fun setupView() {

        loading.setLoader(imageConfiguration.loading())

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }

        next.setOnClickListener {

            viewModel.verifyNumber(
                findNavController(),
                ccp.fullNumberWithPlus,
                phone.text.toString(),
                ccp.selectedCountryNameCode
            )

        }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.phoneVerification.title

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.text = configuration.text.phoneVerification.body

        ccp.setOnCountryChangeListener { viewModel.setCountryNameCode(ccp.selectedCountryNameCode) }
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

            next.isEnabled = checkbox.isChecked && it

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
        phone.addTextChangedListener { setMissingPhoneErrorVisibility(false) }
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

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            next.isEnabled = isChecked && ccp.isValidFullNumber
        }

        setLegalCheckboxText(
            configuration.theme,
            configuration.text.phoneVerification,
            configuration.text.url
        )

        next.background =
            button(resources, imageConfiguration.signUpNextStep())

        missing_number_error.text =
            configuration.text.phoneVerification.error.errorMissingNumber
        missing_number_error.setTextColor(configuration.theme.primaryTextColor.color())
    }

    private fun setLegalCheckboxText(theme: Theme, text: PhoneVerification, url: Url) {

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
                            viewModel.web(
                                findNavController(),
                                if (privacyIndex > termsIndex) url.privacy
                                else url.terms
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
                            viewModel.web(
                                findNavController(),
                                if (privacyIndex > termsIndex) url.terms
                                else url.privacy
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

    private fun setMissingPhoneErrorVisibility(visible: Boolean): Unit {

        viewModel.state().configuration.map {

            phone.setTextColor(
                if (visible) it.theme.primaryTextColor.color()
                else it.theme.secondaryColor.color()
            )
            ccp.contentColor =
                if (visible) it.theme.primaryTextColor.color()
                else it.theme.secondaryColor.color()
        }

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