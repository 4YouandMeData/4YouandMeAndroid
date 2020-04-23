package org.fouryouandme.core.arch.deps

import org.fouryouandme.core.arch.navigation.Navigator

interface Injector {

    /* --- runtime --- */

    val runtimeContext: RuntimeContext

    /* --- navigation --- */

    val navigator: Navigator

    fun getDependencies(): Dependencies
}