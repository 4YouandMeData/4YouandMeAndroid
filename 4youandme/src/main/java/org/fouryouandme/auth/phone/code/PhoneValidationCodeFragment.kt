package org.fouryouandme.auth.phone.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.enter_phone.*
import kotlinx.android.synthetic.main.phone_validation_code.*
import kotlinx.android.synthetic.main.phone_validation_code.ccp
import kotlinx.android.synthetic.main.phone_validation_code.description
import kotlinx.android.synthetic.main.phone_validation_code.loading
import kotlinx.android.synthetic.main.phone_validation_code.next
import kotlinx.android.synthetic.main.phone_validation_code.phone
import kotlinx.android.synthetic.main.phone_validation_code.phone_validation
import kotlinx.android.synthetic.main.phone_validation_code.root
import kotlinx.android.synthetic.main.phone_validation_code.title
import kotlinx.android.synthetic.main.phone_validation_code.toolbar
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*


class PhoneValidationCodeFragment : BaseFragment<PhoneValidationCodeViewModel>(
    R.layout.phone_validation_code
) {

    val args: PhoneValidationCodeFragmentArgs by navArgs()

    override val viewModel: PhoneValidationCodeViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { PhoneValidationCodeViewModel(navigator, IORuntime) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is PhoneValidationCodeStateUpdate.Initialization ->
                        applyConfiguration(it.configuration)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    PhoneValidationCodeLoading.Auth -> loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    PhoneValidationCodeError.Auth -> {

                        when (it.error) {

                            is FourYouAndMeError.WrongPhoneCode ->
                                setWrongCodeErrorVisibility(true)
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
            .fold({ viewModel.initialize() }, { applyConfiguration(it) })

        setupView()
    }

    private fun setupView() {

        loading.setLoader(imageConfiguration.loading())

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }

        phone.setText(args.phone)

        next.setOnClickListener {

            setWrongCodeErrorVisibility(false)
            viewModel.auth(findNavController(), ccp.fullNumberWithPlus, code.text.toString())
        }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

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

        change_phone.setOnClickListener { viewModel.back(findNavController()) }

        code_description.setTextColor(configuration.theme.secondaryColor.color())
        code_description.text =
            configuration.text.phoneVerification.codeDescription

        code.setTextColor(configuration.theme.secondaryColor.color())
        code.setHintTextColor(configuration.theme.secondaryColor.color())
        code.backgroundTintList =
            ColorStateList.valueOf(configuration.theme.secondaryColor.color())
        code.autoCloseKeyboard()
        code.addTextChangedListener {
            code_validation.setImageResource(
                if (it?.toString()?.length == 6) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            next.isEnabled = it?.toString()?.length == 6
        }
        code_validation.setImageResource(
            if (code.text?.toString()?.length == 6) imageConfiguration.entryValid()
            else imageConfiguration.entryWrong()
        )

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

        next.background =
            button(resources, imageConfiguration.signUpNextStep())
        next.isEnabled = code.text?.toString()?.length == 6

        wrong_code_error.text =
            configuration.text.phoneVerification.error.errorWrongCode
        wrong_code_error.setTextColor(configuration.theme.primaryTextColor.color())
    }

    private fun setWrongCodeErrorVisibility(visible: Boolean): Unit {

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