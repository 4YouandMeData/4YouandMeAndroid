package com.foryouandme.ui.auth.signup.later

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.SignUpLaterBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.sign_up_later.*
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpLaterFragment : AuthSectionFragment(R.layout.sign_up_later) {

    private val viewModel: SignUpLaterViewModel by viewModels()

    private val binding: SignUpLaterBinding?
        get() = view?.let { SignUpLaterBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is SignUpLaterStateUpdate.Config -> {
                        setupView()
                        applyConfiguration()
                    }
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    SignUpLaterLoading.Configuration ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    SignUpLaterError.Configuration ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(SignUpLaterStateEvent.GetConfiguration) }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.configuration != null) {
            setupView()
            applyConfiguration()
        } else viewModel.execute(SignUpLaterStateEvent.GetConfiguration)

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(SignUpLaterStateEvent.ScreenViewed)

    }

    private fun setupView() {

        binding?.logo?.setImageResource(imageConfiguration.logo())
        back.setOnClickListener { back() }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = viewModel.state.configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            viewBinding.description.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.description.setHtmlText(configuration.text.signUpLater.body, true)

            viewBinding.divider.setBackgroundColor(configuration.theme.primaryColorEnd.color())

            viewBinding.back.setTextColor(configuration.theme.primaryColorEnd.color())
            viewBinding.back.text = configuration.text.signUpLater.confirmButton
            viewBinding.back.background =
                button(configuration.theme.secondaryColor.color())
        }

    }

}