package org.fouryouandme.auth.questions.screening

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ScreeningQuestionsFragment : BaseFragment<ScreeningQuestionsViewModel>(
    R.layout.screening_questions
) {

    override val viewModel: ScreeningQuestionsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { ScreeningQuestionsViewModel(navigator, IORuntime) }
        )
    }

}