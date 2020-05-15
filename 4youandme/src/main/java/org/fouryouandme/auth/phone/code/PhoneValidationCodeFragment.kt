package org.fouryouandme.auth.phone.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.phone_validation_code.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*


class PhoneValidationCodeFragment : BaseFragment<PhoneValidationCodeViewModel>(
    R.layout.phone_validation_code
) {

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

        viewModel.initialize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration.map { applyConfiguration(it) }
        setupView()
    }

    private fun setupView() {

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
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
        ccp.setAutoDetectedCountry(true)
        ccp.setTextSize(spToPx(15f))
        ccp.setDialogKeyboardAutoPopup(false)
        ccp.registerCarrierNumberEditText(phone)
        ccp.setNumberAutoFormattingEnabled(true)

        phone_validation.imageTintList =
            ColorStateList.valueOf(configuration.theme.secondaryColor.color())
        phone_validation.setImageResource(imageConfiguration.entryValid())

        phone.setTextColor(configuration.theme.secondaryColor.color())
        phone.setHintTextColor(configuration.theme.secondaryColor.color())
        phone.backgroundTintList =
            ColorStateList.valueOf(configuration.theme.secondaryColor.color())
        phone.autoCloseKeyboard()

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
                if (it?.toString()?.length == 4) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )
        }
        code_validation.setImageResource(
            if (code.text?.toString()?.length == 4) imageConfiguration.entryValid()
            else imageConfiguration.entryWrong()
        )

        resend.setTextColor(configuration.theme.secondaryColor.color())
        resend.text = configuration.text.phoneVerification.resendCode

        next.background =
            button(resources, imageConfiguration.signUpNextStep())
    }

    private fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        ).toInt()
    }
}