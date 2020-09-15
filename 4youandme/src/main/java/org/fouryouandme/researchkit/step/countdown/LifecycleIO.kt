package org.fouryouandme.researchkit.step.countdown

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import arrow.fx.IO
import arrow.fx.typeclasses.Disposable
import org.fouryouandme.core.ext.unsafeRunAsyncCancelable

class LifecycleIO(val io: IO<Unit>) : LifecycleObserver {

    private var disposable: Option<Disposable> = None

    init {

        disposable = io.unsafeRunAsyncCancelable().some()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop(): Unit {

        disposable.map { it() }
        disposable = None

    }

    companion object {

        fun IO<Unit>.bindToLifecycle(lifecycleOwner: LifecycleOwner): Unit {

            lifecycleOwner.lifecycle.addObserver(LifecycleIO(this))

        }

    }

}