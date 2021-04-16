package com.foryouandme.ui.aboutyou

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.*
import com.foryouandme.core.ext.find

abstract class AboutYouSectionFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val aboutYouViewModel: AboutYouViewModel by viewModels(ownerProducer = { aboutYouFragment() })

    fun aboutYouFragment(): AboutYouFragment = find()

    fun aboutYouNavController(): AboutYouNavController = AboutYouNavController(findNavController())

    fun back() {
        if(navigator.back(aboutYouNavController()).not())
            navigator.back(rootNavController())
    }

}