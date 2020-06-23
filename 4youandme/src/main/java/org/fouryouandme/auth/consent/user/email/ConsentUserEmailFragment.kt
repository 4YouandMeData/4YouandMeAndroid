package org.fouryouandme.auth.consent.user.email

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_email.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserError
import org.fouryouandme.auth.consent.user.ConsentUserLoading
import org.fouryouandme.auth.consent.user.ConsentUserStateUpdate
import org.fouryouandme.auth.consent.user.ConsentUserViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*

class ConsentUserEmailFragment : BaseFragment<ConsentUserViewModel>(R.layout.consent_user_email) {

    override val viewModel: ConsentUserViewModel by lazy {

        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentUserViewModel(navigator, IORuntime) })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEventPeek {

                when (it) {
                    is ConsentUserStateUpdate.Email -> bindNext()
                }

            }

        viewModel.loadingLiveData()
            .observeEventPeek {

                when (it.task) {
                    ConsentUserLoading.CreateUser -> loading.setVisibility(it.active)
                }

            }

        viewModel.errorLiveData()
            .observeEventPeek {
                when (it.cause) {
                    ConsentUserError.CreateUser -> viewModel.toastError(it.error)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { viewModel.state().configuration.bind() }
            .map { applyConfiguration(it) }

    }

    private fun setupView(): Unit {

        logo.setImageResource(imageConfiguration.logoStudySecondary())

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {
                it.showBackSecondaryButton(imageConfiguration)
                { viewModel.back(findNavController()) }
            }

        next.background = button(resources, imageConfiguration.signUpNextStepSecondary())
        next.setOnClickListener { viewModel.createUser(rootNavController(), findNavController()) }
        bindNext()

    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        email.text = configuration.text.onboarding.user.emailDescription
        email.setTextColor(configuration.theme.primaryTextColor.color())

        email_entry.setBackgroundColor(color(android.R.color.transparent))
        email_entry.setTextColor(configuration.theme.primaryTextColor.color())
        email_entry.addTextChangedListener { viewModel.setEmail(it.toString().trim()) }
        email_entry.setOnFocusChangeListener { _, hasFocus ->

            email_validation.setImageResource(
                when {
                    hasFocus -> 0
                    hasFocus.not() && !viewModel.isValidEmail(email_entry.text.toString().trim()) ->
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

    private fun bindNext(): Unit {

        next.isEnabled =
            viewModel.isValidEmail(viewModel.state().email)

        email_info.isVisible =
            viewModel.isValidEmail(viewModel.state().email)

    }
}