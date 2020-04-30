package org.fouryouandme.core.arch.deps

import android.app.Application
import arrow.Kind
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.typeclasses.Concurrent
import kotlinx.coroutines.CoroutineDispatcher

fun IO.Companion.runtime(injector: Injector, app: Application): Runtime<ForIO> =
    object : Runtime<ForIO>(IO.concurrent(), injector, app) {}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class Runtime<F>(
    concurrent: Concurrent<F>,
    val injector: Injector,
    val app: Application
) : Concurrent<F> by concurrent

data class RuntimeContext(
    val bgDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher
)

fun <F> Runtime<F>.onMainDispatcher(function: () -> Unit): Kind<F, Unit> =
    fx.concurrent {

        continueOn(injector.runtimeContext.mainDispatcher)

        function()

        continueOn(injector.runtimeContext.bgDispatcher)
    }
