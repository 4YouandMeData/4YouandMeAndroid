package com.foryouandme.ui.main

import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.ext.find


abstract class MainSectionOldFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragmentOld<T>(contentLayoutId) {

    protected val mainViewModel: MainViewModel by viewModels({ mainFragment() })


    protected fun mainFragment(): MainFragment = find()

}