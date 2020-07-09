package org.fouryouandme.auth.wearable

import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class WearableFragment : BaseFragment<WearableViewModel>(R.layout.wearable) {

    override val viewModel: WearableViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                WearableViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

}