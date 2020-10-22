package org.fouryouandme.auth.welcome

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*

class WelcomeFragment : AuthSectionFragment<WelcomeViewModel>(R.layout.welcome) {

    override val viewModel: WelcomeViewModel by lazy {
        viewModelFactory(this, getFactory { WelcomeViewModel(navigator) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            setupView()
            applyConfiguration(it)

        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            logo.setImageResource(imageConfiguration.logoStudy())
            welcome_image.setImageResource(imageConfiguration.logoStudySecondary())

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            start.background =
                button(configuration.theme.secondaryColor.color())
            start.text = configuration.text.welcome.startButton
            start.setTextColor(configuration.theme.primaryColorEnd.color())
            start.isAllCaps = false
            start.setOnClickListener {
                startCoroutineAsync { viewModel.signUpInfo(authNavController()) }
            }

            start.alpha = 0f
            start.animate()
                .alpha(1f)
                .setDuration(800L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

        }
}