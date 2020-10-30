package com.foryouandme.core.arch.android

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.core.arch.livedata.EventObserver
import com.foryouandme.core.arch.navigation.RootNavController

abstract class BaseActivity<T : BaseViewModel<*, *, *, *>> : FragmentActivity {

    protected abstract val viewModel: T

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    fun <A> LiveData<Event<A>>.observeEvent(handle: (A) -> Unit): Unit =
        observe(this@BaseActivity, EventObserver { handle(it) })

    fun rootNavController(): RootNavController =
        RootNavController(supportFragmentManager.fragments[0].findNavController())

}