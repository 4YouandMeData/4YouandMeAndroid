package org.fouryouandme.auth.consent.review

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ConsentReviewFragment : BaseFragment<ConsentReviewViewModel>(R.layout.consent_review) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}