package com.foryouandme.auth

import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator

abstract class AuthSectionFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    val authViewModel: AuthViewModel by lazy {
        viewModelFactory(
            authFragment(),
            getFactory { AuthViewModel(navigator) }
        )
    }

    fun authNavController(): AuthNavController = AuthNavController(findNavController())

    private fun authFragment(): AuthFragment = find()

}