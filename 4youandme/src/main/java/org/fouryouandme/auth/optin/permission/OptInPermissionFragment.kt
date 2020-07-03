package org.fouryouandme.auth.optin.permission

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.firstOrNone
import arrow.core.toOption
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_permission.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.configuration.checkbox.checkbox
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton

class OptInPermissionFragment : BaseFragment<OptInViewModel>(R.layout.opt_in_permission) {

    private val args: OptInPermissionFragmentArgs by navArgs()

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                OptInViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().optIns }
            .map { applyConfiguration(it.first, it.second) }

        viewModel.state().permissions[args.id].toOption()
            .map {
                if (it) {
                    agree.isChecked = true
                    agree.jumpDrawablesToCurrentState()
                } else {
                    disagree.isChecked = true
                    disagree.jumpDrawablesToCurrentState()
                }
            }

    }


    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }

    }

    private fun applyConfiguration(configuration: Configuration, optIns: OptIns): Unit {

        optIns.permissions.firstOrNone { it.id == args.id }
            .map {

                root.setBackgroundColor(configuration.theme.secondaryColor.color())

                title.text = it.title
                title.setTextColor(configuration.theme.primaryTextColor.color())

                description.text = it.body
                description.setTextColor(configuration.theme.primaryTextColor.color())

                agree.buttonTintList =
                    checkbox(
                        configuration.theme.primaryColorEnd.color(),
                        configuration.theme.primaryColorEnd.color()
                    )
                agree.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        disagree.isChecked = false
                        viewModel.setPermissionState(args.id, true)
                    }
                }


                agree_text.text = it.agreeText
                agree_text.setTextColor(configuration.theme.primaryTextColor.color())

                disagree.buttonTintList =
                    checkbox(
                        configuration.theme.primaryColorEnd.color(),
                        configuration.theme.primaryColorEnd.color()
                    )
                disagree.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        agree.isChecked = false
                        viewModel.setPermissionState(args.id, false)
                    }
                }

                disagree_text.text = it.disagreeText
                disagree_text.setTextColor(configuration.theme.primaryTextColor.color())

                shadow.background =
                    HEXGradient.from(
                        HEXColor.transparent(),
                        configuration.theme.primaryTextColor
                    ).drawable(0.3f)

                next.text = configuration.text.onboarding.optIn.submitButton
                next.setTextColor(configuration.theme.secondaryColor.color())
                next.background = button(configuration.theme.primaryColorEnd.color())

            }

    }
}