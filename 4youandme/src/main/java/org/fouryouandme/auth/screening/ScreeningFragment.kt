package org.fouryouandme.auth.screening

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.screening.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ScreeningFragment : BaseFragment<ScreeningViewModel>(R.layout.screening) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(this, getFactory { ScreeningViewModel(navigator, IORuntime) })
    }

    fun showAbort(configuration: Configuration, color: Int): Unit {

        abort.text = configuration.text.onboarding.onboardingAbortButton
        abort.setTextColor(color)
        abort.setOnClickListener { showAbortAlert(configuration) }
        abort.isVisible = true

    }

    fun hideAbort(): Unit {

        abort.isVisible = false

    }

    private fun showAbortAlert(configuration: Configuration) {

        AlertDialog.Builder(requireContext())
            .setTitle(configuration.text.onboarding.onboardingAbortTitle)
            .setMessage(configuration.text.onboarding.onboradingAbortMessage)
            .setPositiveButton(configuration.text.onboarding.onboardingAbortConfirm)
            { _, _ -> viewModel.abort(rootNavController()) }
            .setNegativeButton(configuration.text.onboarding.onboardingAbortCancel, null)
            .show()

    }
}