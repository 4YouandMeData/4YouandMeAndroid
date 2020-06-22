package org.fouryouandme.auth.consent.user.name

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.consent_user_name.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserError
import org.fouryouandme.auth.consent.user.ConsentUserStateUpdate
import org.fouryouandme.auth.consent.user.ConsentUserViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*

class ConsentUserNameFragment : BaseFragment<ConsentUserViewModel>(R.layout.consent_user_name) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentUserViewModel(navigator, IORuntime) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ConsentUserStateUpdate.Initialization -> applyConfiguration(it.configuration)
                    is ConsentUserStateUpdate.FirstName -> bindNext()
                    is ConsentUserStateUpdate.LastName -> bindNext()
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active) }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    ConsentUserError.Initialization ->
                        error.setError(it.error) { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration }
            .fold(
                { viewModel.initialize(rootNavController()) },
                { applyConfiguration(it) }
            )

    }

    private fun setupView() {

        bindNext()
        next.background = button(resources, imageConfiguration.signUpNextStep())
        next.setOnClickListener { }

    }

    private fun applyConfiguration(configuration: Configuration): Unit {

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
        first_name_entry.addTextChangedListener { viewModel.setFirstName(it.toString()) }
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
        last_name_entry.addTextChangedListener { viewModel.setLastName(it.toString()) }
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

    private fun bindNext(): Unit {

        next.isEnabled =
            viewModel.state().firstName.isNotEmpty() && viewModel.state().lastName.isNotEmpty()

    }
}