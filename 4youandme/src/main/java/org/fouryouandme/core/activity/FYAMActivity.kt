package org.fouryouandme.core.activity

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseActivity
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class FYAMActivity : BaseActivity<FYAMViewModel>(R.layout.fyam) {

    override val viewModel: FYAMViewModel by lazy {
        viewModelFactory(this, getFactory {
            FYAMViewModel(
                navigator,
                IORuntime
            )
        })
    }
}
