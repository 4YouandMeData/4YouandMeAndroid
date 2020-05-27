package org.fouryouandme.auth.questions.screening

import android.os.Bundle
import android.view.View
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration
            .fold({ viewModel.initialize() }, { })
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

    }
}