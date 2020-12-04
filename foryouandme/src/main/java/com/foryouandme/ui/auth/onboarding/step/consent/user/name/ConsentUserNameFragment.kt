package com.foryouandme.ui.auth.onboarding.step.consent.user.name

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserStateUpdate
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_name.*

class ConsentUserNameFragment : ConsentUserSectionFragment(R.layout.consent_user_name) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ConsentUserStateUpdate.FirstName ->
                        startCoroutineAsync { bindNext() }
                    is ConsentUserStateUpdate.LastName ->
                        startCoroutineAsync { bindNext() }
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

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logConsentUserNameScreenViewed() }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            bindNext()
            action_1.background = button(resources, imageConfiguration.nextStep())
            action_1.setOnClickListenerAsync {

                hideKeyboard()
                viewModel.email(consentUserNavController())

            }

            consentUserFragment().toolbar.removeBackButton()

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorEnd.color())

            root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()


            title.text = configuration.text.onboarding.user.nameTitle
            title.setTextColor(configuration.theme.secondaryColor.color())

            first_name.text = configuration.text.onboarding.user.nameFirstNameDescription
            first_name.setTextColor(configuration.theme.secondaryColor.color())

            last_name.text = configuration.text.onboarding.user.nameLastNameDescription
            last_name.setTextColor(configuration.theme.secondaryColor.color())

            first_name_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            first_name_validation.setImageResource(
                if (first_name_entry.text.toString().isNotEmpty()) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            last_name_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            last_name_validation.setImageResource(
                if (first_name_entry.text.toString().isNotEmpty()) imageConfiguration.entryValid()
                else imageConfiguration.entryWrong()
            )

            first_name_entry.setBackgroundColor(color(android.R.color.transparent))
            first_name_entry.autoCloseKeyboard()
            first_name_entry.setTextColor(configuration.theme.secondaryColor.color())
            first_name_entry.addTextChangedListener {
                startCoroutineAsync { viewModel.setFirstName(it.toString()) }
            }
            first_name_entry.setOnFocusChangeListener { _, hasFocus ->
                first_name_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && first_name_entry.text.toString().isEmpty() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            last_name_entry.setBackgroundColor(color(android.R.color.transparent))
            last_name_entry.autoCloseKeyboard()
            last_name_entry.setTextColor(configuration.theme.secondaryColor.color())
            last_name_entry.addTextChangedListener {
                startCoroutineAsync { viewModel.setLastName(it.toString()) }
            }
            last_name_entry.setOnFocusChangeListener { _, hasFocus ->
                last_name_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && last_name_entry.text.toString().isEmpty() ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            first_name_line.setBackgroundColor(configuration.theme.secondaryColor.color())
            last_name_line.setBackgroundColor(configuration.theme.secondaryColor.color())

        }

    private suspend fun bindNext(): Unit =
        evalOnMain {

            action_1.isEnabled =
                viewModel.state().firstName.isNotEmpty() && viewModel.state().lastName.isNotEmpty()

        }
}