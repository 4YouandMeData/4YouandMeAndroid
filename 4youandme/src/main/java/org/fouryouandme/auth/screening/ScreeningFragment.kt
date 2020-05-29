package org.fouryouandme.auth.screening

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ScreeningFragment : BaseFragment<ScreeningViewModel>() {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(this, getFactory { ScreeningViewModel(navigator, IORuntime) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration
            .fold(
                { viewModel.initialize(findNavController()) },
                { Unit }
            )
    }
}