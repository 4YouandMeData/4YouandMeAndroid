package org.fouryouandme.core.arch.android

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.Kind
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.ErrorPayload
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.arch.loading.LoadingPayload
import org.fouryouandme.core.arch.navigation.Navigator

object Empty

open class BaseViewModel<F, S, SU, E, A>(
    private var state: S? = null,
    protected val navigator: Navigator,
    protected val runtime: Runtime<F>
) : ViewModel(), LifecycleObserver {

    /* ============= STATE ============= */

    fun state(): S = state!!

    fun isInitialized(): Boolean = state != null

    private val stateLiveData = MutableLiveData<Event<SU>>()

    @Deprecated(message = "use the suspend version")
    protected fun setState(newState: S, update: SU): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                state = newState
                stateLiveData.value = update.toEvent()
            }
        }

    // TODO: delete tho old version and rename this
    protected suspend fun setStateFx(newState: S, update: (S) -> SU): Unit {

        evalOn(Dispatchers.Main) {
            state = newState
            stateLiveData.value = update(newState).toEvent()
        }

    }

    protected suspend fun setStateSilentFx(newState: S): Unit {

        evalOn(Dispatchers.Main) {
            state = newState
        }

    }

    fun stateLiveData(): LiveData<Event<SU>> = stateLiveData

    /* ============= ERROR ============= */

    private val errors = MutableLiveData<Event<ErrorPayload<E>>>()

    @Deprecated(message = "use the suspend version")
    protected fun setError(error: FourYouAndMeError, cause: E): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                errors.value = ErrorPayload(cause, error).toEvent()
            }
        }

    // TODO: delete tho old version and rename this
    protected suspend fun setErrorFx(error: FourYouAndMeError, cause: E): Unit {

        evalOn(Dispatchers.Main) { errors.value = ErrorPayload(cause, error).toEvent() }

    }

    fun errorLiveData(): LiveData<Event<ErrorPayload<E>>> = errors

    /* ============= LOADING ============= */

    private val loading = MutableLiveData<Event<LoadingPayload<A>>>()

    @Deprecated(message = "use the suspend version")
    protected fun showLoading(task: A): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                loading.value = LoadingPayload(task, true).toEvent()
            }

        }

    // TODO: delete tho old version and rename this
    protected suspend fun showLoadingFx(task: A): Unit {

        evalOn(Dispatchers.Main) { loading.value = LoadingPayload(task, true).toEvent() }

    }

    @Deprecated(message = "use the suspend version")
    protected fun hideLoading(task: A): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                loading.value = LoadingPayload(task, false).toEvent()
            }
        }

    // TODO: delete tho old version and rename this
    protected suspend fun hideLoadingFx(task: A): Unit {

        evalOn(Dispatchers.Main) { loading.value = LoadingPayload(task, false).toEvent() }

    }

    fun loadingLiveData(): LiveData<Event<LoadingPayload<A>>> = loading

    /* ============= NAVIGATION ============= */

    fun activityActions(): LiveData<Event<(FragmentActivity) -> Unit>> = navigator.activityAction()

    /* ============= RES ============= */

    protected fun getString(@StringRes stringRes: Int): String =
        runtime.app.getString(stringRes)
}