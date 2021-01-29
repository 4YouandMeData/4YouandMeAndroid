package com.foryouandme.ui.main

import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find


abstract class MainSectionFragment(
    contentLayoutId: Int
) : BaseFragment(contentLayoutId) {

    protected val mainViewModel: MainViewModel by viewModels({ mainFragment() })

    private fun mainFragment(): MainFragment = find()

}