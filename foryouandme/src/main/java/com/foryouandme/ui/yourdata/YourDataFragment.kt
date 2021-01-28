package com.foryouandme.ui.yourdata

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.core.ext.*
import com.foryouandme.ui.yourdata.items.*
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.your_data.*

class YourDataFragment : BaseFragmentOld<YourDataViewModel>(R.layout.your_data) {

    override val viewModel: YourDataViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                YourDataViewModel(
                    navigator,
                    injector.yourDataModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            YourDataHeaderViewHolder.factory(),
            YourDataButtonsViewHolder.factory
            { period ->
                configuration { viewModel.selectPeriod(rootNavController(), it, period) }
            },
            YourDataGraphViewHolder.factory(),
            YourDataGraphErrorViewHolder.factory {
                configuration { viewModel.updateGraphs(rootNavController(), it) }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { stateUpdate ->
                when (stateUpdate) {
                    is YourDataStateUpdate.Initialization ->
                        startCoroutineAsync { applyItems(stateUpdate.items) }
                    is YourDataStateUpdate.Period ->
                        startCoroutineAsync { applyItems(stateUpdate.items) }

                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    YourDataLoading.Initialization ->
                        loading.setVisibility(it.active, viewModel.isInitialized())
                    YourDataLoading.Period ->
                        loading.setVisibility(it.active, true)
                }
            }


        viewModel.errorLiveData()
            .observeEvent(name()) { errorPayload ->
                when (errorPayload.cause) {
                    YourDataError.Initialization ->
                        error.setError(errorPayload.error) {
                            configuration {
                                viewModel.initialize(
                                    rootNavController(),
                                    it,
                                )
                            }
                        }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

    }

    override fun onResume() {
        super.onResume()

        configuration {

            applyConfiguration(it)

            viewModel.initialize(rootNavController(), it)

        }

    }

    private suspend fun applyConfiguration(
        configuration: Configuration
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