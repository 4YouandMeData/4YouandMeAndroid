package org.fouryouandme.auth.consent.informed

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.consent_info.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ConsentInfoFragment : BaseFragment<ConsentInfoViewModel>(R.layout.consent_info) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    fun showAbort(configuration: Configuration, color: Int): Unit {

        abort.text = configuration.text.onboarding.abortButton
        abort.setTextColor(color)
        abort.setOnClickListener { showAbortAlert(configuration) }
        abort.isVisible = true

    }

    fun hideAbort(): Unit {

        abort.isVisible = false

    }

    private fun showAbortAlert(configuration: Configuration) {

        AlertDialog.Builder(requireContext())
            .setTitle(configuration.text.onboarding.abortTitle)
            .setMessage(configuration.text.onboarding.abortMessage)
            .setPositiveButton(configuration.text.onboarding.abortConfirm)
            { _, _ -> viewModel.abort(rootNavController()) }
            .setNegativeButton(configuration.text.onboarding.abortCancel, null)
            .show()

    }

}