package org.fouryouandme.auth.optin.permission

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_permission.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInError
import org.fouryouandme.auth.optin.OptInLoading
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
import org.fouryouandme.core.ext.setStatusBar

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    OptInLoading.PermissionSet -> loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    OptInError.PermissionSet -> viewModel.toastError(it.error)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        viewModel.state().permissions[args.index].toOption()
            .map {
                if (it) {
                    agree.isChecked = true
                    agree.jumpDrawablesToCurrentState()
                } else {
                    disagree.isChecked = true
                    disagree.jumpDrawablesToCurrentState()
                }
            }

        Option.fx { !viewModel.state().configuration to !viewModel.state().optIns }
            .map { applyConfiguration(it.first, it.second) }

    }


    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }

    }

    private fun applyConfiguration(configuration: Configuration, optIns: OptIns): Unit {

        optIns.permissions.getOrNull(args.index).toOption()
            .map { permission ->

                setStatusBar(configuration.theme.secondaryColor.color())

                root.setBackgroundColor(configuration.theme.secondaryColor.color())

                val decodedString =
                    permission.image.map { Base64.decode(it, Base64.DEFAULT) }
                val decodedByte =
                    decodedString.map { BitmapFactory.decodeByteArray(it, 0, it.size) }

                decodedByte.map { icon.setImageBitmap(it) }

                title.text = permission.title
                title.setTextColor(configuration.theme.primaryTextColor.color())

                description.text = permission.body
                description.setTextColor(configuration.theme.primaryTextColor.color())

                agree.buttonTintList =
                    checkbox(
                        configuration.theme.primaryColorEnd.color(),
                        configuration.theme.primaryColorEnd.color()
                    )
                agree.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        disagree.isChecked = false
                        viewModel.setPermissionState(args.index, true)
                        next.isEnabled = agree.isChecked || disagree.isChecked
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
                        viewModel.setPermissionState(args.index, false)
                        next.isEnabled = agree.isChecked || disagree.isChecked
                    }
                }

                disagree_text.text = permission.disagreeText
                disagree_text.setTextColor(configuration.theme.primaryTextColor.color())

                shadow.background =
                    HEXGradient.from(
                        HEXColor.transparent(),
                        configuration.theme.primaryTextColor
                    ).drawable(0.3f)

                next.text = configuration.text.onboarding.optIn.submitButton
                next.setTextColor(configuration.theme.secondaryColor.color())
                next.background = button(configuration.theme.primaryColorEnd.color())
                next.setOnClickListener {
                    viewModel.requestPermissions(
                        rootNavController(),
                        findNavController(),
                        Dexter.withContext(requireContext()),
                        args.index
                    )
                }
                next.isEnabled = agree.isChecked || disagree.isChecked
            }
    }

}