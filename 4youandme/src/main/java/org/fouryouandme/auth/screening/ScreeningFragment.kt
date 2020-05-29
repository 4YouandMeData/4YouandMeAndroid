package org.fouryouandme.auth.screening

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ScreeningFragment : BaseFragment<ScreeningViewModel>(R.layout.screening) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(this, getFactory { ScreeningViewModel(navigator, IORuntime) })
    }
}