package com.fouryouandme.auth

import androidx.navigation.fragment.findNavController
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.navigator

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