package com.fouryouandme.main

import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.navigator


abstract class MainSectionFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    protected val mainViewModel: MainViewModel by lazy {
        viewModelFactory(mainFragment(), getFactory { MainViewModel(navigator) })
    }


    protected fun mainFragment(): MainFragment = find()

}