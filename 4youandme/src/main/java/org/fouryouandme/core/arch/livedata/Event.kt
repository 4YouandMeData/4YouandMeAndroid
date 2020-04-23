package org.fouryouandme.core.arch.livedata

import arrow.core.Either
import arrow.core.left
import arrow.core.right

open class Event<T>(private val content: T) {

    private var handle: Handle<T> = ToHandle(content)

    /**
     * Returns the content and prevents its use again.
     */
    fun getContent(): Either<Handled<T>, ToHandle<T>> {

        val value =
                when (val value = handle) {
                    is ToHandle -> value.right()
                    is Handled -> value.left()
                }

        handle = Handled()

        return value
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

fun <T> T.toEvent() = Event(this)


sealed class Handle<T>

data class ToHandle<T>(val t: T) : Handle<T>()

class Handled<T> : Handle<T>()