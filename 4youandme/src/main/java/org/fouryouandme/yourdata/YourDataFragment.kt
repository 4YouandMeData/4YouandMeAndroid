package org.fouryouandme.yourdata

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.your_data.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.*
import org.fouryouandme.yourdata.items.*

class YourDataFragment : BaseFragment<YourDataViewModel>(R.layout.your_data) {

    override val viewModel: YourDataViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                YourDataViewModel(
                    navigator,
                    IORuntime,
                    injector.yourDataModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            YourDataHeaderViewHolder.factory(),
            YourDataButtonsViewHolder.factory
            { startCoroutineAsync { viewModel.selectPeriod(it) } },
            YourDataGraphViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { stateUpdate ->
                when (stateUpdate) {
                    is YourDataStateUpdate.Initialization ->
                        configuration { applyConfiguration(it, stateUpdate.items) }
                    is YourDataStateUpdate.Period ->
                        startCoroutineAsync { applyItems(stateUpdate.items) }

                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    YourDataLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }


        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    YourDataError.Initialization ->
                        error.setError(it.error) {
                            startCoroutineAsync {
                                viewModel.initialize(
                                    rootNavController(),
                                    configuration(),
                                    imageConfiguration
                                )
                            }
                        }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        configuration {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), it, imageConfiguration)
            else
                applyConfiguration(it, viewModel.state().items)

        }
    }

    private suspend fun applyConfiguration(
        configuration: Configuration,
        items: List<DroidItem<Any>>
    ) {
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.fourthColor.color())

            title.text = configuration.text.tab.userDataTitle
            title.setTextColor(configuration.theme.secondaryColor.color())
            title.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            adapter.submitList(items)
        }
    }

    private fun setupRecyclerView() {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        recycler_view.adapter = adapter

        recycler_view.addItemDecoration(
            LinearMarginItemDecoration(
                topMargin = {
                    when {
                        it.isOfType<YourDataHeaderItem>() -> 0.dpToPx()
                        else -> 40.dpToPx()
                    }
                },
                startMargin = {
                    when {
                        it.isOfType<YourDataHeaderItem>() -> 0.dpToPx()
                        it.isOfType<YourDataGraphItem>() -> 0.dpToPx()
                        else -> 20.dpToPx()
                    }
                },
                endMargin = {
                    when {
                        it.isOfType<YourDataHeaderItem>() -> 0.dpToPx()
                        it.isOfType<YourDataGraphItem>() -> 0.dpToPx()
                        else -> 20.dpToPx()
                    }
                },
                bottomMargin = {
                    it.isLast(40.dpToPx(), 0.dpToPx())
                }
            )
        )

    }

    private suspend fun applyItems(items: List<DroidItem<Any>>): Unit =
        evalOnMain { adapter.submitList(items) }

}