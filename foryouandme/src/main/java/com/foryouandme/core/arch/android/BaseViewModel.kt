package com.foryouandme.core.arch.android

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.fx.coroutines.evalOn
import com.foryouandme.core.arch.error.ErrorPayload
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.loading.LoadingPayload
import com.foryouandme.core.arch.navigation.Navigator
import kotlinx.coroutines.Dispatchers

object Empty

open class BaseViewModel<S, SU, E, A>(
    protected val navigator: Navigator,
    private var state: S? = null,
) : ViewModel(), LifecycleObserver {

    /* ============= STATE ============= */

    fun state(): S = state!!

    fun isInitialized(): Boolean = state != null

    private val stateLiveData = MutableLiveData<Event<SU>>()

    protected suspend fun setState(newState: S, update: (S) -> SU): Unit {

        evalOn(Dispatchers.Main) {
            state = newState
            stateLiveData.value = update(newState).toEvent()
        }

    }

    protected suspend fun setStateSilent(newState: S): Unit {

        evalOn(Dispatchers.Main) {
            state = newState
        }

    }

    fun stateLiveData(): LiveData<Event<SU>> = stateLiveData

    /* ============= ERROR ============= */

    private val errors = MutableLiveData<Event<ErrorPayload<E>>>()

    protected suspend fun setError(error: ForYouAndMeError, cause: E): Unit {

        evalOn(Dispatchers.Main) { errors.value = ErrorPayload(cause, error).toEvent() }

    }

    fun errorLiveData(): LiveData<Event<ErrorPayload<E>>> = errors

    /* ============= LOADING ============= */

    private val loading = MutableLiveData<Event<LoadingPayload<A>>>()

    protected suspend fun showLoading(task: A): Unit {

        evalOn(Dispatchers.Main) { loading.value = LoadingPayload(task, true).toEvent() }

    }

    protected suspend fun hideLoading(task: A): Unit {

        evalOn(Dispatchers.Main) { loading.value = LoadingPayload(task, false).toEvent() }

    }

    fun loadingLiveData(): LiveData<Event<LoadingPayload<A>>> = loading

    /* ============= NAVIGATION ============= */

    fun activityActions() = navigator.activityAction()

}