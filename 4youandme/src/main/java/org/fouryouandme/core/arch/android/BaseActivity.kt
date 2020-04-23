package org.fouryouandme.core.arch.android

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.EventObserver

abstract class BaseActivity<T : BaseViewModel<*, *, *, *, *>> : FragmentActivity {

    protected abstract val viewModel: T

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    fun <A> LiveData<Event<A>>.observeEvent(handle: (A) -> Unit): Unit =
        observe(this@BaseActivity, EventObserver { handle(it) })
}