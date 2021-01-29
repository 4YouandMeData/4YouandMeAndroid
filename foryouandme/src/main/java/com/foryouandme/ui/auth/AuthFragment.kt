package com.foryouandme.ui.auth

import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragmentOld<AuthViewModel>(R.layout.auth) {

    override val viewModel: AuthViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { AuthViewModel(navigator) }
        )
    }

}