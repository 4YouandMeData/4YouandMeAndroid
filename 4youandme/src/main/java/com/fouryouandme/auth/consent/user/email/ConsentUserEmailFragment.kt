package com.fouryouandme.auth.consent.user.email

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.fouryouandme.R
import com.fouryouandme.auth.consent.user.ConsentUserError
import com.fouryouandme.auth.consent.user.ConsentUserLoading
import com.fouryouandme.auth.consent.user.ConsentUserSectionFragment
import com.fouryouandme.auth.consent.user.ConsentUserStateUpdate
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.configuration.button.button
import com.fouryouandme.core.ext.*
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_email.*

class ConsentUserEmailFragment : ConsentUserSectionFragment(R.layout.consent_user_email) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) {

                when (it) {
                    is ConsentUserStateUpdate.Email ->
                        startCoroutineAsync { bindNext() }
                }

            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {

                when (it.task) {
                    ConsentUserLoading.CreateUser ->
                        consent_user_email_loading.setVisibility(it.active)
                }

            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    ConsentUserError.CreateUser ->
                        startCoroutineAsync { viewModel.toastError(it.error) }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentUserAndConfiguration { config, _ ->

            setupView()
            applyConfiguration(config)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            logo.setImageResource(imageConfiguration.logoStudySecondary())

            consentUserFragment()
                .toolbar
                .showBackSecondaryButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            consentUserNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }


            next.background =
                button(resources, imageConfiguration.nextStepSecondary())

            next.setOnClickListener {
                startCoroutineAsync {
                    viewModel.createUser(
                        rootNavController(),
                        consentUserNavController()
                    )
                }
            }

            bindNext()

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            email.text = configuration.text.onboarding.user.emailDescription
            email.setTextColor(configuration.theme.primaryTextColor.color())

            email_entry.setBackgroundColor(color(android.R.color.transparent))
            email_entry.setTextColor(configuration.theme.primaryTextColor.color())
            email_entry.addTextChangedListener {
                startCoroutineAsync { viewModel.setEmail(it.toString().trim()) }
            }
            email_entry.setOnFocusChangeListener { _, hasFocus ->

                email_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && !viewModel.isValidEmail(
                            email_entry.text.toString().trim()
                        ) ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            email_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.primaryTextColor.color())
            email_validation.setImageResource(
                if (viewModel.isValidEmail(email_entry.text.toString().trim()))
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            email_line.setBackgroundColor(configuration.theme.primaryTextColor.color())

            email_info.text = configuration.text.onboarding.user.emailInfo
            email_info.setTextColor(configuration.theme.fourthTextColor.color())
        }

    private suspend fun bindNext(): Unit =
        evalOnMain {

            next.isEnabled =
                viewModel.isValidEmail(viewModel.state().email)

            email_info.isVisible =
                viewModel.isValidEmail(viewModel.state().email)

        }
}