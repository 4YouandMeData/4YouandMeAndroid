package org.fouryouandme.researchkit.step.countdown

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import arrow.fx.typeclasses.Disposable
import org.fouryouandme.core.ext.startCoroutineCancellableAsync

class LifecycleCoroutine(val block: suspend () -> Unit) : LifecycleObserver {

    private var disposable: Disposable? = null

    init {

        disposable = startCoroutineCancellableAsync(block)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop(): Unit {

        disposable?.let { it() }
        disposable = null

    }

    companion object {

        fun startLifecycleCoroutineAsync(
            lifecycleOwner: LifecycleOwner,
            block: suspend () -> Unit
        ): Unit {

            lifecycleOwner.lifecycle.addObserver(LifecycleCoroutine(block))

        }

    }

}