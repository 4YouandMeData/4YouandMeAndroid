package org.fouryouandme.auth.splash

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.splash.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class SplashFragment : BaseFragment<SplashViewModel>(R.layout.splash) {

    override val viewModel: SplashViewModel by lazy {
        viewModelFactory(this, getFactory { SplashViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize(findNavController())

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    SplashError.Configuration ->
                        error.setError(it.error) { viewModel.initialize(findNavController()) }
                }
            }
    }
}