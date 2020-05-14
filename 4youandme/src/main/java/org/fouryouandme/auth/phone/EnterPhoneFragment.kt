package org.fouryouandme.auth.phone

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.enter_phone.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
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

        viewModel.initialize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration.map { applyConfiguration(it) }
        setupView()
    }

    private fun setupView() {

        logo.setImageResource(imageConfiguration.logo())

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.phoneVerificationTitle

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.text = configuration.text.phoneVerificationBody

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
        ccp.setPhoneNumberValidityChangeListener {

            phone_validation.setImageResource(
                if (it) imageConfiguration.phoneValid()
                else imageConfiguration.phoneWrong()
            )

            next.isEnabled = checkbox.isChecked && it
        }

        phone_validation.imageTintList =
            ColorStateList.valueOf(configuration.theme.secondaryColor.color())

        phone.setTextColor(configuration.theme.secondaryColor.color())
        phone.setHintTextColor(configuration.theme.secondaryColor.color())
        phone.backgroundTintList =
            ColorStateList.valueOf(configuration.theme.secondaryColor.color())
        phone.autoCloseKeyboard()

        checkbox.buttonTintList =
            checkbox(
                configuration.theme.secondaryColor.color(),
                configuration.theme.secondaryColor.color()
            )

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            next.isEnabled = isChecked && ccp.isValidFullNumber
        }

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