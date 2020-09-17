package org.fouryouandme.auth.consent.review.disagree

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.consent_review_disagree.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.review.ConsentReviewViewModel
import org.fouryouandme.core.arch.android.BaseDialogFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.background.roundBackground
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.consent.review.ConsentReview
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator

class ConsentReviewDisagreeFragment : BaseDialogFragment<ConsentReviewViewModel>() {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.consent_review_disagree, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().consentReview to !viewModel.state().configuration }
            .map { applyConfiguration(it.first, it.second) }
    }

    private fun setupView(): Unit {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        close_recording.setImageResource(imageConfiguration.close())
        close_recording.setOnClickListener { viewModel.back(findNavController()) }

        disagree.setOnClickListener { viewModel.exit(rootNavController()) }

    }

    private fun applyConfiguration(
        consentReview: ConsentReview,
        configuration: Configuration
    ): Unit {

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