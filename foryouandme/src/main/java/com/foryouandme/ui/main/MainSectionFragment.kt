package com.foryouandme.ui.main

import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator


abstract class MainSectionFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragmentOld<T>(contentLayoutId) {

    protected val mainViewModel: MainViewModel by lazy {
        viewModelFactory(
            mainFragment(),
            getFactory { MainViewModel(navigator, injector.analyticsModule()) }
        )
    }


    protected fun mainFragment(): MainFragment = find()

}