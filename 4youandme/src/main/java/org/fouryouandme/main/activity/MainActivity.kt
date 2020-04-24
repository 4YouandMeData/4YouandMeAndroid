package org.fouryouandme.main.activity

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseActivity
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class MainActivity : BaseActivity<MainViewModel>(R.layout.main) {

    override val viewModel: MainViewModel by lazy {
        viewModelFactory(this, getFactory {
            MainViewModel(
                navigator,
                IORuntime
            )
        })
    }
}
