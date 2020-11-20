package com.foryouandme.auth.onboarding.step.consent.optin.permission

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.auth.onboarding.step.consent.optin.OptInError
import com.foryouandme.auth.onboarding.step.consent.optin.OptInLoading
import com.foryouandme.auth.onboarding.step.consent.optin.OptInSectionFragment
import com.foryouandme.auth.onboarding.step.consent.optin.OptInState
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXColor
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.configuration.checkbox.checkbox
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_permission.*

class OptInPermissionFragment : OptInSectionFragment(R.layout.opt_in_permission) {

    private val args: OptInPermissionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    OptInLoading.PermissionSet -> permission_loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    OptInError.PermissionSet ->
                        startCoroutineAsync { viewModel.toastError(it.error) }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optInAndConfiguration { config, state ->

            setupView()
            applyConfiguration(config, state)

        }

    }


    private suspend fun setupView(): Unit =
        evalOnMain {

            optInFragment().toolbar.removeBackButton()

        }

    private suspend fun applyConfiguration(configuration: Configuration, state: OptInState): Unit =
        evalOnMain {

            state.permissions[args.index]
                ?.let {
                    if (it) {
                        agree.isChecked = true
                        agree.jumpDrawablesToCurrentState()
                    } else {
                        disagree.isChecked = true
                        disagree.jumpDrawablesToCurrentState()
                    }
                }

            state.optIns.permissions.getOrNull(args.index)
                ?.let { permission ->

                    setStatusBar(configuration.theme.secondaryColor.color())

                    root.setBackgroundColor(configuration.theme.secondaryColor.color())

                    val decodedString =
                        permission.image.map { Base64.decode(it, Base64.DEFAULT) }
                    val decodedByte =
                        decodedString.map { BitmapFactory.decodeByteArray(it, 0, it.size) }

                    decodedByte.map { icon.setImageBitmap(it) }

                    title.setHtmlText(permission.title, true)
                    title.setTextColor(configuration.theme.primaryTextColor.color())

                    description.setHtmlText(permission.body, true)
                    description.setTextColor(configuration.theme.primaryTextColor.color())

                    agree.buttonTintList =
                        checkbox(
                            configuration.theme.primaryColorEnd.color(),
                            configuration.theme.primaryColorEnd.color()
                        )
                    agree.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            disagree.isChecked = false
                            startCoroutineAsync {
                                viewModel.setPermissionState(args.index, true)
                            }
                            action_1.isEnabled = agree.isChecked || disagree.isChecked
                        }
                    }


                    agree_text.text = permission.agreeText
                    agree_text.setTextColor(configuration.theme.primaryTextColor.color())

                    disagree.buttonTintList =
                        checkbox(
                            configuration.theme.primaryColorEnd.color(),
                            configuration.theme.primaryColorEnd.color()
                        )
                    disagree.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            agree.isChecked = false
                            startCoroutineAsync {
                                viewModel.setPermissionState(args.index, false)
                            }
                            action_1.isEnabled = agree.isChecked || disagree.isChecked
                        }
                    }

                    disagree_text.text = permission.disagreeText
                    disagree_text.setTextColor(configuration.theme.primaryTextColor.color())

                    shadow.background =
                        HEXGradient.from(
                            HEXColor.transparent(),
                            configuration.theme.primaryTextColor
                        ).drawable(0.3f)

                    action_1.text = configuration.text.onboarding.optIn.submitButton
                    action_1.setTextColor(configuration.theme.secondaryColor.color())
                    action_1.background = button(configuration.theme.primaryColorEnd.color())
                    action_1.setOnClickListener {
                        configuration {
                            viewModel.requestPermissions(
                                it,
                                rootNavController(),
                                optInNavController(),
                                args.index
                            )
                        }
                    }
                    action_1.isEnabled = agree.isChecked || disagree.isChecked
                }
        }

}