package org.fouryouandme.auth.consent.user.name

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_name.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserSectionFragment
import org.fouryouandme.auth.consent.user.ConsentUserStateUpdate
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*

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

    private suspend fun setupView(): Unit =
        evalOnMain {

            bindNext()
            next.background = button(resources, imageConfiguration.signUpNextStep())
            next.setOnClickListener {
                startCoroutineAsync { viewModel.email(consentUserNavController()) }
            }

            consentUserFragment().toolbar.removeBackButton()

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

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

            next.isEnabled =
                viewModel.state().firstName.isNotEmpty() && viewModel.state().lastName.isNotEmpty()

        }
}