package com.foryouandme.auth

import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.navigator

class AuthFragment : BaseFragment<AuthViewModel>(R.layout.auth) {

    override val viewModel: AuthViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { AuthViewModel(navigator) }
        )
    }

}