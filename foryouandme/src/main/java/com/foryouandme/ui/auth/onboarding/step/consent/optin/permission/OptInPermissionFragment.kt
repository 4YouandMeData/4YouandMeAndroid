package com.foryouandme.ui.auth.onboarding.step.consent.optin.permission

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.OptInPermissionBinding
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.configuration.checkbox.checkbox
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInError
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInLoading
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OptInPermissionFragment : OptInSectionFragment(R.layout.opt_in_permission) {

    private val args: OptInPermissionFragmentArgs by navArgs()

    private val binding: OptInPermissionBinding?
        get() = view?.let { OptInPermissionBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    OptInLoading.Permission -> binding?.permissionLoading?.setVisibility(it.active)
                    OptInLoading.OptIn -> applyData()
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    OptInError.Permission -> errorToast(it.error, configuration)
                    OptInError.OptIn -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setUpView() {

        optInFragment().binding?.toolbar?.removeBackButton()

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration

        if (viewBinding != null && configuration != null) {

            viewModel.state.permissions[args.index]
                ?.let {
                    if (it) {
                        viewBinding.agree.isChecked = true
                        viewBinding.agree.jumpDrawablesToCurrentState()
                    } else {
                        viewBinding.disagree.isChecked = true
                        viewBinding.disagree.jumpDrawablesToCurrentState()
                    }
                }

            viewModel.state.optIns?.permissions?.getOrNull(args.index)
                ?.let { permission ->

                    setStatusBar(configuration.theme.secondaryColor.color())

                    viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

                    val decodedString =
                        permission.image?.let { Base64.decode(it, Base64.DEFAULT) }
                    val decodedByte =
                        decodedString?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

                    decodedByte?.let { viewBinding.icon.setImageBitmap(it) }

                    viewBinding.title.setHtmlText(permission.title, true)
                    viewBinding.title.setTextColor(configuration.theme.primaryTextColor.color())

                    viewBinding.description.setHtmlText(permission.body, true)
                    viewBinding.description.setTextColor(configuration.theme.primaryTextColor.color())

                    viewBinding.agree.buttonTintList =
                        checkbox(
                            configuration.theme.primaryColorEnd.color(),
                            configuration.theme.primaryColorEnd.color()
                        )
                    viewBinding.agree.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            binding?.disagree?.isChecked = false
                            viewModel.execute(OptInStateEvent.SetPermission(args.index, true))
                            binding?.action1?.isEnabled =
                                binding?.agree?.isChecked == true ||
                                        binding?.disagree?.isChecked == true
                        }
                    }


                    viewBinding.agreeText.text = permission.agreeText
                    viewBinding.agreeText.setTextColor(configuration.theme.primaryTextColor.color())

                    viewBinding.disagree.buttonTintList =
                        checkbox(
                            configuration.theme.primaryColorEnd.color(),
                            configuration.theme.primaryColorEnd.color()
                        )
                    viewBinding.disagree.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            binding?.agree?.isChecked = false
                            viewModel.execute(OptInStateEvent.SetPermission(args.index, false))
                        }
                        binding?.action1?.isEnabled =
                            binding?.agree?.isChecked == true ||
                                    binding?.disagree?.isChecked == true
                    }

                    viewBinding.disagreeText.text = permission.disagreeText
                    viewBinding.disagreeText.setTextColor(configuration.theme.primaryTextColor.color())

                    viewBinding.shadow.background =
                        HEXGradient.from(
                            HEXColor.transparent(),
                            configuration.theme.primaryTextColor
                        ).drawable(0.3f)

                    viewBinding.action1.text = configuration.text.onboarding.optIn.submitButton
                    viewBinding.action1.setTextColor(configuration.theme.secondaryColor.color())
                    viewBinding.action1.background =
                        button(configuration.theme.primaryColorEnd.color())
                    viewBinding.action1.setOnClickListener {
                        viewModel.execute(
                            OptInStateEvent.RequestPermission(
                                configuration,
                                args.index
                            )
                        )
                    }
                    viewBinding.action1.isEnabled =
                        viewBinding.agree.isChecked || viewBinding.disagree.isChecked
                }
        }
    }

}