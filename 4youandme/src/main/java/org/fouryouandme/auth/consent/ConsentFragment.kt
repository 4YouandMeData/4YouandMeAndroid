package org.fouryouandme.auth.consent

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ConsentFragment : BaseFragment<ConsentViewModel>(R.layout.consent) {

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { ConsentViewModel(navigator, IORuntime) }
        )
    }

}