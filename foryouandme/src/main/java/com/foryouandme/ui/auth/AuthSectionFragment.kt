package com.foryouandme.ui.auth

import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.*
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator

abstract class AuthSectionFragment : BaseFragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    fun authNavController(): AuthNavController = AuthNavController(findNavController())

    private fun authFragment(): AuthFragment = find()

}