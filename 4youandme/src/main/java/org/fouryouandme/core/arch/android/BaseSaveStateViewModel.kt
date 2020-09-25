package org.fouryouandme.core.arch.android

import androidx.lifecycle.SavedStateHandle
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

abstract class BaseSaveStateViewModel<F, S, SU, E, A>(
 navigator: Navigator,
 runtime: Runtime<F>,
 private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<F, S, SU, E, A>(navigator = navigator, runtime = runtime)