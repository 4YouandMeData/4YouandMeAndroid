package com.foryouandme.researchkit.step.chooseone

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.background.shadow
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.common.QuestionViewHolder
import com.foryouandme.researchkit.utils.applyImageAsButton
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.step_choose_one.*
import org.threeten.bp.ZonedDateTime

class ChooseOneStepFragment : StepFragment(R.layout.step_choose_one) {

    private val chooseOneStepViewModel: ChooseOneViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { ChooseOneViewModel(navigator) }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            QuestionViewHolder.factory(),
            ChooseOneAnswerViewHolder.factory {

                startCoroutineAsync {

                    chooseOneStepViewModel.answer(it.id)

                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseOneStepViewModel.stateLiveData()
            .observeEvent(name()) {
                when (it) {
                    is ChooseOneStepStateUpdate.Initialization -> applyItems(it.items)
                    is ChooseOneStepStateUpdate.Answer -> applyItems(it.items)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            setupRecyclerView()

            val step =
                viewModel.getStepByIndexAs<ChooseOneStep>(indexArg())

            step?.let {

                if (chooseOneStepViewModel.isInitialized().not())
                    chooseOneStepViewModel.initialize(it, it.values)

                applyData(it)
            }
        }
    }

    private suspend fun applyData(
        step: ChooseOneStep
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

                val answer =
                    chooseOneStepViewModel.getSelectedAnswer()

                answer?.let {

                    startCoroutineAsync {

                        viewModel.addResult(

                            SingleAnswerResult(
                                step.identifier,
                                start,
                                ZonedDateTime.now(),
                                step.questionId,
                                it.id,
                            )

                        )

                        checkSkip(step, answer)
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

        button.isEnabled = chooseOneStepViewModel.getSelectedAnswer() != null

        adapter.submitList(items)
    }

    private suspend fun checkSkip(step: ChooseOneStep, answerItem: ChooseOneAnswerItem): Unit =
        evalOnMain {

            val skip = step.skips.firstOrNull { it.answerId == answerItem.id }

            if (skip != null) skipTo(skip.stepId)
            else next()

        }

}