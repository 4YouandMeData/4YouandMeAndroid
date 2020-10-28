package org.fouryouandme.core.arch.android

import android.content.ServiceConnection
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import arrow.core.Either
import org.fouryouandme.core.activity.FYAMActivity
import org.fouryouandme.core.activity.FYAMState
import org.fouryouandme.core.activity.FYAMViewModel
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.EventObserver
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskInjector


abstract class BaseFragment<T : BaseViewModel<*, *, *, *>> : Fragment {

    protected abstract val viewModel: T

    protected val fyamViewModel: FYAMViewModel by lazy {

        viewModelFactory(
            requireActivity(),
            getFactory {
                FYAMViewModel(
                    navigator,
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

    fun unbindService(serviceConnection: ServiceConnection): Unit =
        startCoroutineAsync {
            Either.catch {
                evalOnMain { requireActivity().applicationContext.unbindService(serviceConnection) }
            }
        }

    fun name(): String = this.javaClass.simpleName

    fun fyamActivity(): FYAMActivity = requireActivity() as FYAMActivity

    suspend fun fyamState(): FYAMState =
        if (fyamViewModel.isInitialized())
            fyamViewModel.state()
        else
            fyamViewModel.initialize(
                rootNavController(),
                fyamActivity().taskIdArg(),
                fyamActivity().urlArg(),
                fyamActivity().openAppIntegrationArg()
            ).orNull()!!

    fun configuration(block: suspend (Configuration) -> Unit): Unit =
        startCoroutineAsync {

            val configuration = fyamState().configuration

            block(configuration)

        }

    fun fyamState(block: suspend (FYAMState) -> Unit): Unit =
        startCoroutineAsync { block(fyamState()) }

    fun taskConfiguration(): TaskConfiguration =
        (requireContext().applicationContext as TaskInjector).provideBuilder()

}