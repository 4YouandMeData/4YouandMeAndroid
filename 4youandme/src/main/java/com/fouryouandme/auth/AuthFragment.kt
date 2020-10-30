package com.fouryouandme.auth

import com.fouryouandme.R
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.ext.navigator

class AuthFragment : BaseFragment<AuthViewModel>(R.layout.auth) {

    override val viewModel: AuthViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { AuthViewModel(navigator) }
        )
    }

}