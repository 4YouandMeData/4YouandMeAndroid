package com.foryouandme.ui.auth.onboarding.step.consent.user.email

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.ConsentUserEmailBinding
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.consent.user.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserEmailFragment : ConsentUserSectionFragment(R.layout.consent_user_email) {

    private val binding: ConsentUserEmailBinding?
        get() = view?.let { ConsentUserEmailBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentUserStateUpdate.Email -> bindNext()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {

                when (it.task) {
                    ConsentUserLoading.CreateUser ->
                        binding?.consentUserEmailLoading?.setVisibility(it.active)
                    else ->
                        Unit
                }

            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ConsentUserError.CreateUser -> errorToast(it.error, configuration)
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

        viewModel.execute(ConsentUserAction.ConsentEmailViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.logo.setImageResource(imageConfiguration.logoStudySecondary())

            consentUserFragment()
                .binding
                ?.toolbar
                ?.showBackSecondaryButton(imageConfiguration) { back() }


            viewBinding.action1.background =
                button(resources, imageConfiguration.nextStepSecondary())

            viewBinding.action1.setOnClickListener {

                hideKeyboard()
                viewModel.execute(ConsentUserAction.CreateUser)

            }

            bindNext()

        }
    }

    private fun applyConfiguration() {

        val configuration = configuration
        val viewBinding = binding

        if (configuration != null && viewBinding != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.email.text = configuration.text.onboarding.user.emailDescription
            viewBinding.email.setTextColor(configuration.theme.primaryTextColor.color())

            viewBinding.emailEntry.setBackgroundColor(color(android.R.color.transparent))
            viewBinding.emailEntry.setTextColor(configuration.theme.primaryTextColor.color())
            viewBinding.emailEntry.addTextChangedListener {
                viewModel.execute(ConsentUserAction.SetEmail(it.toString().trim()))
            }
            viewBinding.emailEntry.setOnFocusChangeListener { _, hasFocus ->

                binding?.emailValidation?.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && viewModel.state.isValidEmail.not() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            viewBinding.emailValidation.imageTintList =
                ColorStateList.valueOf(configuration.theme.primaryTextColor.color())
            viewBinding.emailValidation.setImageResource(
                if (viewModel.state.isValidEmail)
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            viewBinding.emailLine.setBackgroundColor(configuration.theme.primaryTextColor.color())

            viewBinding.emailInfo.text = configuration.text.onboarding.user.emailInfo
            viewBinding.emailInfo.setTextColor(configuration.theme.fourthTextColor.color())
        }
    }

    private fun bindNext() {

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.action1.isEnabled = viewModel.state.isValidEmail

            viewBinding.emailInfo.isVisible = viewModel.state.isValidEmail
        }
    }
}