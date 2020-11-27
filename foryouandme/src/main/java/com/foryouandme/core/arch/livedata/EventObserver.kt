package com.foryouandme.core.arch.livedata

import androidx.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(
    private val handlerId: String? = null,
    private val onEventUnhandledContent: (T) -> Unit
) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>) {

        if (handlerId == null)
            event.getContentOnce()
                .map { onEventUnhandledContent(it.item) }
        else
            event.getContentByHandler(handlerId)
                .map { onEventUnhandledContent(it.item) }
    }
}

