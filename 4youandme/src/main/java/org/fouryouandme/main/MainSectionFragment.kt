package org.fouryouandme.main

import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.navigator


abstract class MainSectionFragment<T : BaseViewModel<*, *, *, *, *>>(
    contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    protected val mainViewModel: MainViewModel by lazy {
        viewModelFactory(mainFragment(), getFactory { MainViewModel(navigator, IORuntime) })
    }


    protected fun mainFragment(): MainFragment = find()

}