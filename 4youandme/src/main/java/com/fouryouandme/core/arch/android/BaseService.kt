package com.fouryouandme.core.arch.android

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import com.fouryouandme.core.arch.livedata.Event
import com.fouryouandme.core.arch.livedata.EventObserver


abstract class BaseService : LifecycleService() {

    fun <A> LiveData<Event<A>>.observeEvent(handlerId: String? = null, handle: (A) -> Unit): Unit =
        observe(this@BaseService, EventObserver(handlerId) { handle(it) })

    fun <A> LiveData<Event<A>>.observeEventPeek(handle: (A) -> Unit): Unit =
        observe(this@BaseService, { handle(it.peekContent()) })

}