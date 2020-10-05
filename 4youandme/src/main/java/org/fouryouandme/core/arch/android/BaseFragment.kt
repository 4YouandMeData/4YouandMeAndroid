package org.fouryouandme.core.arch.android

import android.content.ServiceConnection
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import arrow.fx.IO
import arrow.fx.extensions.fx
import org.fouryouandme.core.activity.FYAMViewModel
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.EventObserver
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskInjector


abstract class BaseFragment<T : BaseViewModel<*, *, *, *, *>> : Fragment {

    protected abstract val viewModel: T

    protected val fyamViewModel: FYAMViewModel by lazy {

        viewModelFactory(
            requireActivity(),
            getSavedFactory(requireActivity()) {
                FYAMViewModel(
                    navigator,
                    IORuntime,
                    it,
                    injector.configurationModule()
                )
            }
        )

    }

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.activityActions()
            .observeEvent { it(requireActivity()) }

    }

    fun rootNavController(): RootNavController =
        RootNavController(requireActivity().supportFragmentManager.fragments[0].findNavController())

    fun <A> LiveData<Event<A>>.observeEvent(handlerId: String? = null, handle: (A) -> Unit): Unit =
        observe(this@BaseFragment, EventObserver(handlerId) { handle(it) })

    fun <A> LiveData<Event<A>>.observeEventPeek(handle: (A) -> Unit): Unit =
        observe(this@BaseFragment, { handle(it.peekContent()) })

    fun unbindServiceIO(serviceConnection: ServiceConnection): Unit =

        IO.fx { requireActivity().applicationContext.unbindService(serviceConnection) }
            .attempt()
            .unsafeRunAsync()

    fun name(): String = this.javaClass.simpleName

    suspend fun configuration(): Configuration =
        if (fyamViewModel.isInitialized())
            fyamViewModel.state().configuration
        else
            fyamViewModel.initialize(rootNavController()).orNull()!!

    fun configuration(block: suspend (Configuration) -> Unit): Unit =
        startCoroutineAsync {

            val configuration = configuration()

            block(configuration)

        }

    fun taskConfiguration(): TaskConfiguration =
        (requireContext().applicationContext as TaskInjector).provideBuilder()

}