package com.foryouandme.core.arch.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

open class UIEvent<T>(private val content: T) {

    private var handle: Handle<T> = Handle.ToHandle(content)
    private var handledBy: List<String> = emptyList()

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentOnce(): Handle<T> {

        val value =
                when (val value = handle) {
                    is Handle.ToHandle -> value
                    is Handle.Handled -> value
                }

        handle = Handle.Handled()

        return value
    }

    /**
     * Returns the content and prevents its use again from the same handler.
     */
    fun getContentByHandler(handlerId: String): Handle<T> =

            when (val value = handle) {
                is Handle.ToHandle -> {
                    if (handledBy.firstOrNull { it == handlerId } == null) {
                        handledBy = handledBy.plus(handlerId)
                        value
                    } else Handle.Handled()
                }
                is Handle.Handled ->
                    value
            }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    /**
     * Check if two events have the same content
     */
    fun haveTheSameContent(other: UIEvent<T>): Boolean =
            peekContent() == other.peekContent()
}

fun <T> T.toUIEvent() = UIEvent(this)


sealed class Handle<T> {

    data class ToHandle<T>(val t: T) : Handle<T>()

    class Handled<T> : Handle<T>()

    fun getOrNull(): T? =
            when (this) {
                is ToHandle<T> -> this.t
                is Handled -> null
            }

}

fun <T : Any> Flow<UIEvent<T>>.unwrapEvent(handlerId: String): Flow<T> =
        mapNotNull { it.getContentByHandler(handlerId).getOrNull() }