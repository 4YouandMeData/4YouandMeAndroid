package org.fouryouandme.auth.consent.user.email.code

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_email_validation_code.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserError
import org.fouryouandme.auth.consent.user.ConsentUserLoading
import org.fouryouandme.auth.consent.user.ConsentUserSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*

class ConsentUserEmailValidationCodeFragment : ConsentUserSectionFragment(
    R.layout.consent_user_email_validation_code
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    ConsentUserLoading.ConfirmEmail ->
                        consent_user_email_loading.setVisibility(it.active)
                    ConsentUserLoading.ResendConfirmationEmail ->
                        consent_user_email_loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    ConsentUserError.ConfirmEmail ->
                        startCoroutineAsync { viewModel.toastError(it.error) }
                    ConsentUserError.ResendConfirmationEmail ->
                        startCoroutineAsync { viewModel.toastError(it.error) }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentUserAndConfiguration { config, state ->

            setupView()
            applyConfiguration(config)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            consentUserFragment()
                .toolbar
                .showBackButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            consentUserNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }

        next.background = button(resources, imageConfiguration.signUpNextStep())
            next.setOnClickListener {
                startCoroutineAsync {
                    viewModel.confirmEmail(
                        rootNavController(),
                        consentUserNavController(),
                        code_entry.text.toString().trim()
                    )
                }
            }

            resend.setOnClickListener {
                startCoroutineAsync { viewModel.resendEmail(rootNavController()) }
            }

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.activeColor.color())

            root.setBackgroundColor(configuration.theme.activeColor.color())

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.onboarding.user.emailVerificationTitle

            description.setTextColor(configuration.theme.secondaryColor.color())
            description.text = configuration.text.onboarding.user.emailVerificationBody

            email.text = configuration.text.onboarding.user.emailVerificationWrongMail
            email.setTextColor(configuration.theme.secondaryColor.color())

            email_entry.setBackgroundColor(color(android.R.color.transparent))
            email_entry.setTextColor(configuration.theme.secondaryColor.color())
            email_entry.setText(viewModel.state().email)
            email.isEnabled = false

            email_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            email_validation.setImageResource(imageConfiguration.entryValid())

            email_line.setBackgroundColor(configuration.theme.secondaryColor.color())

            change_email.setOnClickListener {
                startCoroutineAsync {
                    viewModel.back(
                        consentUserNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

            code.text = configuration.text.onboarding.user.emailVerificationCodeDescription
            code.setTextColor(configuration.theme.secondaryColor.color())

            code_entry.setBackgroundColor(color(android.R.color.transparent))
            code_entry.setTextColor(configuration.theme.secondaryColor.color())
            code_entry.addTextChangedListener {
                next.isEnabled = it.toString().trim().length == 6
            }
            code_entry.setOnFocusChangeListener { _, hasFocus ->

                code_validation.setImageResource(
                    when {
                        hasFocus -> 0
                        hasFocus.not() && code_entry.text.toString().trim().length != 6 ->
                            imageConfiguration.entryWrong()
                        else ->
                            imageConfiguration.entryValid()
                    }
                )
            }

            code_validation.imageTintList =
                ColorStateList.valueOf(configuration.theme.secondaryColor.color())
            code_validation.setImageResource(
                if (code_entry.text.toString().length == 6)
                    imageConfiguration.entryValid()
                else
                    imageConfiguration.entryWrong()
            )

            code_line.setBackgroundColor(configuration.theme.secondaryColor.color())

            resend.text = SpanDroid().append(
                configuration.text.onboarding.user.emailVerificationResend,
                spanList(requireContext()) {
                    click {}
                    typeface(R.font.helvetica)
                    custom(ForegroundColorSpan(configuration.theme.secondaryColor.color()))
                    custom(UnderlineSpan())
                }
            ).toSpannableString()

            next.isEnabled = code_entry.text.toString().trim().length == 6

        }
}