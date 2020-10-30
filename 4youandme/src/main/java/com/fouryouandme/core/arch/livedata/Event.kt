package com.fouryouandme.core.arch.livedata

import arrow.core.Either
import arrow.core.firstOrNone
import arrow.core.left
import arrow.core.right

open class Event<T>(private val content: T) {

    private var handle: Handle<T> = ToHandle(content)
    private var handledBy: List<String> = emptyList()

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentOnce(): Either<Handled<T>, ToHandle<T>> {

        val value =
            when (val value = handle) {
                is ToHandle -> value.right()
                is Handled -> value.left()
            }

        handle = Handled()

        return value
    }

    /**
     * Returns the content and prevents its use again from the same handler.
     */
    fun getContentByHandler(handlerId: String): Either<Handled<T>, ToHandle<T>> =

        when (val value = handle) {
            is ToHandle -> {
                if (handledBy.firstOrNone { it == handlerId }.isEmpty()) {
                    handledBy = handledBy.plus(handlerId)
                    value.right()
                } else Handled<T>().left()
            }
            is Handled ->
                value.left()
        }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    /**
     * Check if two events have the same content
     */
    fun haveTheSameContent(other: Event<T>): Boolean =
        peekContent() == other.peekContent()
}

fun <T> T.toEvent() = Event(this)


sealed class Handle<T>

data class ToHandle<T>(val t: T) : Handle<T>()

class Handled<T> : Handle<T>()