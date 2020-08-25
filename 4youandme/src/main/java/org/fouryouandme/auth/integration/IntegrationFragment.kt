package org.fouryouandme.auth.integration

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class IntegrationFragment : BaseFragment<IntegrationViewModel>(R.layout.integration) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                IntegrationViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}