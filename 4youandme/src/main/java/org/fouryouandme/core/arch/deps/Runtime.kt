package org.fouryouandme.core.arch.deps

import android.app.Application
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.typeclasses.Concurrent
import kotlinx.coroutines.CoroutineDispatcher

fun IO.Companion.runtime(ctx: RuntimeContext, dependencies: Dependencies): Runtime<ForIO> =
    object : Runtime<ForIO>(IO.concurrent(), ctx, dependencies) {}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class Runtime<F>(
    concurrent: Concurrent<F>,
    val context: RuntimeContext,
    val dependencies: Dependencies
) : Concurrent<F> by concurrent

data class RuntimeContext(
    val bgDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher
)

data class Dependencies constructor(val application: Application)

