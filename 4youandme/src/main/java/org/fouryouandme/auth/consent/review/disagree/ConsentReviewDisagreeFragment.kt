package org.fouryouandme.auth.consent.review.disagree

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.consent_review_disagree.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.review.ConsentReviewSectionDialogFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.background.roundBackground
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.consent.review.ConsentReview
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.startCoroutineAsync

class ConsentReviewDisagreeFragment : ConsentReviewSectionDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.consent_review_disagree, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentReviewAndConfiguration { config, state ->

            setupView()
            applyConfiguration(config, state.consentReview)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            close_recording.setImageResource(imageConfiguration.close())
            close_recording.setOnClickListener {
                startCoroutineAsync {
                    viewModel.back(
                        consentReviewNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

            disagree.setOnClickListener {
                startCoroutineAsync { viewModel.exit(rootNavController()) }
            }

        }

    private suspend fun applyConfiguration(
        configuration: Configuration,
        consentReview: ConsentReview
    ): Unit =
        evalOnMain {

            root.background =
                roundBackground(configuration.theme.secondaryColor.color())

            body.text = consentReview.disagreeModalBody
            body.setTextColor(configuration.theme.primaryTextColor.color())

            disagree.text = consentReview.disagreeModalButton
            disagree.setTextColor(configuration.theme.secondaryColor.color())
            disagree.background =
                button(configuration.theme.primaryColorEnd.color())

        }
}