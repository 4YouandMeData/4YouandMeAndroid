package org.fouryouandme.main.feeds

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.feeds.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.*
import org.fouryouandme.main.MainSectionFragment
import org.fouryouandme.main.items.DateViewHolder
import org.fouryouandme.main.items.QuickActivitiesItem
import org.fouryouandme.main.items.QuickActivitiesViewHolder
import org.fouryouandme.main.items.TaskActivityViewHolder
import org.fouryouandme.researchkit.task.TaskHandleResult


class FeedsFragment : MainSectionFragment<FeedsViewModel>(R.layout.feeds) {

    override val viewModel: FeedsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                FeedsViewModel(
                    navigator,
                    IORuntime,
                    injector.feedModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory {
                startCoroutineAsync { viewModel.executeTasks(rootNavController(), it) }
            },
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory(),
            FeedHeaderViewHolder.factory(),
            FeedEmptyViewHolder.factory()

            //TODO: aggiungere feeds

        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent { state ->
                when (state) {
                    is FeedsStateUpdate.Initialization ->
                        configuration { applyData(it, state.feeds) }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent {
                error.setError(it.error) {
                    startCoroutineAsync {
                        viewModel.initialize(rootNavController(), configuration())
                    }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        configuration {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), configuration())

            applyData(it, viewModel.state().feeds)

        }

    }

    override fun onResume() {
        super.onResume()

        taskConfiguration().taskResultLiveData
            .value
            ?.getContentByHandler(name())
            ?.let { event ->
                event.map {
                    if (it.t is TaskHandleResult.Handled)
                        startCoroutineAsync {
                            viewModel.initialize(rootNavController(), configuration())
                        }
                }
            }
    }

    private suspend fun applyData(configuration: Configuration, tasks: List<DroidItem<Any>>): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            header.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            title.text = "Week 12"
            title.setTextColor(configuration.theme.secondaryTextColor.color())

            header_text.text = "2ND TRIMESTER"
            header_text.setTextColor(configuration.theme.secondaryColor.color())
            header_text.alpha = 0.5f

            logo.setImageResource(imageConfiguration.logoStudySecondary())
            logo.setOnClickListener {
                startCoroutineAsync {
                    viewModel.aboutYouPage(rootNavController())
                }
            }

            applyTasks(tasks)
        }

    private fun applyTasks(tasks: List<DroidItem<Any>>): Unit {

        adapter.submitList(tasks)

    }

    private fun setupList(): Unit {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.invalidateItemDecorations()
        recycler_view.addItemDecoration(
            LinearMarginItemDecoration(
                topMargin = {
                    when {
                        it.isOfType<FeedEmptyItem>() -> 50.dpToPx()
                        else -> 0.dpToPx()
                    }
                },
                startMargin = {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                endMargin = {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                bottomMargin = {
                    when {
                        it.isOfType<FeedEmptyItem>() -> 20.dpToPx()
                        else -> 0.dpToPx()
                    }
                }
            )
        )

    }
}