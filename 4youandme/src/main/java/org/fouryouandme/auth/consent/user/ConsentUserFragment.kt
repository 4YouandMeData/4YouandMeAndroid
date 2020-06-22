package org.fouryouandme.auth.consent.user

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ConsentUserFragment : BaseFragment<ConsentUserViewModel>(R.layout.consent_user) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentUserViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}