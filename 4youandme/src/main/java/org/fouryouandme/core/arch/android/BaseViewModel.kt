package org.fouryouandme.core.arch.android

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.Kind
import arrow.fx.ForIO
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.ErrorPayload
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.arch.loading.LoadingPayload
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.ext.unsafeRunAsync

object Empty

open class BaseViewModel<F, S, SU, E, A>(
    private var state: S,
    protected val navigator: Navigator,
    protected val runtime: Runtime<F>
) : ViewModel(), LifecycleObserver {

    /* ============= STATE ============= */

    fun state(): S = state

    private val stateLiveData = MutableLiveData<Event<SU>>()

    protected fun setState(newState: S, update: SU): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                state = newState
                stateLiveData.value = update.toEvent()
            }
        }

    fun stateLiveData(): LiveData<Event<SU>> = stateLiveData

    /* ============= ERROR ============= */

    private val errors = MutableLiveData<Event<ErrorPayload<E>>>()

    protected fun setError(error: FourYouAndMeError, cause: E): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                errors.value = ErrorPayload(cause, error).toEvent()
            }
        }

    fun errorLiveData(): LiveData<Event<ErrorPayload<E>>> = errors

    /* ============= LOADING ============= */

    private val loading = MutableLiveData<Event<LoadingPayload<A>>>()

    protected fun showLoading(task: A): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                loading.value = LoadingPayload(task, true).toEvent()
            }

        }

    protected fun hideLoading(task: A): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {
                loading.value = LoadingPayload(task, false).toEvent()
            }
        }

    fun loadingLiveData(): LiveData<Event<LoadingPayload<A>>> = loading

    /* ============= NAVIGATION ============= */

    fun activityActions(): LiveData<Event<(FragmentActivity) -> Unit>> = navigator.activityAction()

    /* ============= RES ============= */

    protected fun getString(@StringRes stringRes: Int): String =
        runtime.app.getString(stringRes)
}