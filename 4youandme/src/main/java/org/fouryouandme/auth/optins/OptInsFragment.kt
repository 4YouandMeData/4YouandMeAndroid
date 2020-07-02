package org.fouryouandme.auth.optins

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class OptInsFragment : BaseFragment<OptInsViewModel>(R.layout.opt_ins) {

    override val viewModel: OptInsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                OptInsViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}