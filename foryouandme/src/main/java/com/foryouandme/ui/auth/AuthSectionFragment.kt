package com.foryouandme.ui.auth

import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator

abstract class AuthSectionFragment<T : BaseViewModel<*, *, *, *>> : BaseFragmentOld<T> {

    val authViewModel: AuthViewModel by lazy {
        viewModelFactory(
            authFragment(),
            getFactory { AuthViewModel(navigator) }
        )
    }

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    fun authNavController(): AuthNavController = AuthNavController(findNavController())

    private fun authFragment(): AuthFragment = find()

}