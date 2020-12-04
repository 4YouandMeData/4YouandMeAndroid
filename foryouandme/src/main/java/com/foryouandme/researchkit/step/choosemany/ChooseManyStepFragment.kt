package com.foryouandme.researchkit.step.choosemany

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.result.MultipleAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.common.QuestionViewHolder
import com.foryouandme.researchkit.utils.applyImageAsButton
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.step_choose_many.*
import org.threeten.bp.ZonedDateTime

class ChooseManyStepFragment : StepFragment(R.layout.step_choose_many) {

    private val chooseManyStepViewModel: ChooseManyViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { ChooseManyViewModel(navigator) }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            QuestionViewHolder.factory(),
            ChooseManyAnswerViewHolder.factory {

                startCoroutineAsync {

                    chooseManyStepViewModel.answer(it.id)

                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseManyStepViewModel.stateLiveData()
            .observeEvent(name()) {
                when (it) {
                    is ChooseManyStepStateUpdate.Initialization -> applyItems(it.items)
                    is ChooseManyStepStateUpdate.Answer -> applyItems(it.items)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            setupRecyclerView()

            val step =
                viewModel.getStepByIndexAs<ChooseManyStep>(indexArg())

            step?.let {

                if (chooseManyStepViewModel.isInitialized().not())
                    chooseManyStepViewModel.initialize(it, it.values)

                applyData(it)
            }
        }
    }

    private suspend fun applyData(
        step: ChooseManyStep
    ): Unit =
        evalOnMain {

            val start = ZonedDateTime.now()

            root.setBackgroundColor(step.backgroundColor)

            shadow.background = shadow(step.shadowColor)

            button.applyImageAsButton(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync { next() }
            }

            button.setOnClickListener {

                val answers =
                    chooseManyStepViewModel.getSelectedAnswers()

                if (answers.isNotEmpty()) {

                    startCoroutineAsync {

                        viewModel.addResult(
                            MultipleAnswerResult(
                                step.identifier,
                                start,
                                ZonedDateTime.now(),
                                step.questionId,
                                answers.map { it.id }
                            )
                        )

                        checkSkip(step, answers)
                    }

                }
            }
        }

    private suspend fun setupRecyclerView(): Unit =

        evalOnMain {

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            recycler_view.adapter = adapter

            recycler_view.addItemDecoration(
                LinearMarginItemDecoration(
                    {
                        if (it.index == 0) 30.dpToPx()
                        else 0.dpToPx()
                    },
                    { 20.dpToPx() },
                    { 20.dpToPx() },
                    {
                        if (it.index == it.itemCount) 30.dpToPx()
                        else 0.dpToPx()
                    }
                )
            )

        }

    private fun applyItems(items: List<DroidItem<Any>>): Unit {

        button.isEnabled = chooseManyStepViewModel.getSelectedAnswers().isEmpty().not()

        adapter.submitList(items)
    }

    private suspend fun checkSkip(step: ChooseManyStep, answers: List<ChooseManyAnswerItem>): Unit =
        evalOnMain {

            val answersId = answers.map { it.id }

            val skip =
                step.skips.firstOrNull { answersId.contains(it.answerId) }

            if (skip != null) skipTo(skip.stepId)
            else next()

        }

}