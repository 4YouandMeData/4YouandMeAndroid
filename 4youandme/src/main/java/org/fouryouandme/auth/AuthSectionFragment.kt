package org.fouryouandme.auth

import androidx.navigation.fragment.findNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.navigator

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