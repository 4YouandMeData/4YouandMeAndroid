package org.fouryouandme.auth.optin

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class OptInFragment : BaseFragment<OptInViewModel>(R.layout.opt_in) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                OptInViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}