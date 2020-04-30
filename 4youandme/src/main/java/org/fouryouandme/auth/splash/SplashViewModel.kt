package org.fouryouandme.auth.splash

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class SplashViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<ForIO, Empty, Empty, SplashError, SplashLoading>
    (Empty, navigator, runtime) {

    fun initialize(): Unit =

        runtime.fx.concurrent {

            !showLoading(SplashLoading.Configuration)

            val configuration =
                !ConfigurationUseCase.getTheme(runtime, CachePolicy.DiskFirst)

            !configuration.fold({ setError(it, SplashError.Configuration) }, { just(Unit) })

            !hideLoading(SplashLoading.Configuration)

        }.unsafeRunAsync()
}