package org.fouryouandme.auth

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.navigator

class AuthFragment : BaseFragment<AuthViewModel>(R.layout.auth) {

    override val viewModel: AuthViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { AuthViewModel(navigator) }
        )
    }

}