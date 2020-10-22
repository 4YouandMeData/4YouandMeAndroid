package org.fouryouandme.core.arch.android

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.error.ErrorPayload
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.arch.loading.LoadingPayload
import org.fouryouandme.core.arch.navigation.Navigator

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

    protected suspend fun setError(error: FourYouAndMeError, cause: E): Unit {

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

    fun activityActions(): LiveData<Event<(FragmentActivity) -> Unit>> = navigator.activityAction()

}