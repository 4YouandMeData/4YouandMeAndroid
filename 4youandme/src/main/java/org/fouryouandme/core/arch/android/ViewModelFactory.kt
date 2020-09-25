package org.fouryouandme.core.arch.android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner

inline fun <reified VM : BaseViewModel<*, *, *, *, *>> viewModelFactory(
    fragment: Fragment,
    factory: ViewModelFactory<VM>
): VM =
    ViewModelProviders.of(fragment, factory).get(VM::class.java)

inline fun <reified VM : BaseViewModel<*, *, *, *, *>> viewModelFactory(
    activity: FragmentActivity,
    factory: ViewModelFactory<VM>
): VM =
    ViewModelProviders.of(activity, factory).get(VM::class.java)

inline fun <reified VM : BaseViewModel<*, *, *, *, *>> viewModelFactory(
    activity: FragmentActivity,
    factory: AbstractSavedStateViewModelFactory
): VM =
    ViewModelProviders.of(activity, factory).get(VM::class.java)


class ViewModelFactory<V : BaseViewModel<*, *, *, *, *>?>(
    val factory: () -> V
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
}

fun <V : BaseViewModel<*, *, *, *, *>> getFactory(factory: () -> V): ViewModelFactory<V> =
    ViewModelFactory(factory)

fun <V : BaseViewModel<*, *, *, *, *>> getSavedFactory(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    factory: (SavedStateHandle) -> V
): AbstractSavedStateViewModelFactory =
    object : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = factory(handle) as T

    }