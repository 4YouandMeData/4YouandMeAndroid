package com.foryouandme.ui.auth.onboarding.step.consent.user.name

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.ConsentUserNameBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserAction
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserNameToConsentUserEmail
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserNameFragment : ConsentUserSectionFragment(R.layout.consent_user_name) {

    private val binding: ConsentUserNameBinding?
        get() = view?.let { ConsentUserNameBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentUserStateUpdate.FirstName -> bindNext()
                    is ConsentUserStateUpdate.LastName -> bindNext()
                    else -> Unit
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

        viewModel.execute(ConsentUserAction.ConsentUserNameViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {
            bindNext()
            viewBinding.action1.background = button(resources, imageConfiguration.nextStep())
            viewBinding.action1.setOnClickListener {

                hideKeyboard()
                navigator.navigateTo(
                    consentUserNavController(),
                    ConsentUserNameToConsentUserEmail
                )

            }

            consentUserFragment().binding?.toolbar?.removeBackButton()

        }

    }

    private fun applyConfiguration() {

        val configuration = configuration
        val viewBinding = binding

        if (configuration != null && viewBinding != null) {

            setStatusBar(configuration.theme.primaryColorEnd.color())

            viewBinding.root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()


            viewBinding.title.text = configuration.text.onboarding.user.nameTitle
            viewBinding.title.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.body.text = configuration.text.onboarding.user.nameBody
            viewBinding.body.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.firstName.text = configuration.text.onboarding.user.nameFirstNameDescription
            viewBinding.firstName.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.lastName.text = configuration.text.onboarding.user.nameLastNameDescription
            viewBinding.lastName.setTextColor(configuration.theme.secondaryColor.color())

            viewBinding.firstNameValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            viewBinding.firstNameValidation.setImageResource(
                if (viewBinding.firstNameEntry.text.toString().isNotEmpty())
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            viewBinding.lastNameValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            viewBinding.lastNameValidation.setImageResource(
                if (viewBinding.lastNameEntry.text.toString().isNotEmpty())
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            viewBinding.firstNameEntry.setBackgroundColor(color(android.R.color.transparent))
            viewBinding.firstNameEntry.autoCloseKeyboard()
            viewBinding.firstNameEntry.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.firstNameEntry.addTextChangedListener {
                viewModel.execute(ConsentUserAction.SetFirstName(it.toString()))
            }
            viewBinding.firstNameEntry.setOnFocusChangeListener { _, hasFocus ->
                binding?.firstNameValidation?.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && binding?.firstNameEntry?.text.toString().isEmpty() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            viewBinding.lastNameEntry.setBackgroundColor(color(android.R.color.transparent))
            viewBinding.lastNameEntry.autoCloseKeyboard()
            viewBinding.lastNameEntry.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.lastNameEntry.addTextChangedListener {
                viewModel.execute(ConsentUserAction.SetLastName(it.toString()))
            }
            viewBinding.lastNameEntry.setOnFocusChangeListener { _, hasFocus ->
                binding?.lastNameValidation?.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && binding?.lastNameEntry?.text.toString().isEmpty() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            viewBinding.firstNameLine.setBackgroundColor(configuration.theme.secondaryColor.color())
            viewBinding.lastNameLine.setBackgroundColor(configuration.theme.secondaryColor.color())

        }
    }

    private fun bindNext() {

        binding?.action1?.isEnabled =
            viewModel.state.firstName.isNotEmpty() &&
                    viewModel.state.lastName.isNotEmpty()

    }
}