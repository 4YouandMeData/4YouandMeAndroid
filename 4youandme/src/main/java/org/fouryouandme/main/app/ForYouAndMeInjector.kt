package org.fouryouandme.main.app

import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.Dependencies
import org.fouryouandme.core.arch.deps.Injector
import org.fouryouandme.core.arch.deps.RuntimeContext
import org.fouryouandme.core.arch.navigation.Navigator

class ForYouAndMeInjector(val app: FourYouAndMeApp) : Injector {

    override val runtimeContext: RuntimeContext =
        RuntimeContext(Dispatchers.IO, Dispatchers.Main)

    override val navigator: Navigator =
        Navigator(ForYouAndMeNavigationProvider())

    override fun getDependencies(): Dependencies = Dependencies(app)
}