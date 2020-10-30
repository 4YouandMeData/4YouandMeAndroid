package com.foryouandme.main

import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator


abstract class MainSectionFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    protected val mainViewModel: MainViewModel by lazy {
        viewModelFactory(mainFragment(), getFactory { MainViewModel(navigator) })
    }


    protected fun mainFragment(): MainFragment = find()

}