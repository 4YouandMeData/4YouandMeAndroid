package com.foryouandme.researchkit.step.chooseone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.databinding.StepChooseOneBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.AnswerResult
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.common.QuestionViewHolder
import com.foryouandme.entity.resources.applyImageAsButton
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.ZonedDateTime

@AndroidEntryPoint
class ChooseOneStepFragment : StepFragment(R.layout.step_choose_one) {

    private val chooseOneStepViewModel: ChooseOneViewModel by viewModels()

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            QuestionViewHolder.factory(),
            ChooseOneAnswerViewHolder.factory(
                { chooseOneStepViewModel.execute(ChooseOneStepStateEvent.Answer(it.id)) },
                { item, text ->
                    chooseOneStepViewModel.execute(
                        ChooseOneStepStateEvent.AnswerTextChange(item.id, text)
                    )
                }
            )
        )
    }

    private val binding: StepChooseOneBinding?
        get() = view?.let { StepChooseOneBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseOneStepViewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ChooseOneStepStateUpdate.Initialization -> applyItems(it.items)
                    is ChooseOneStepStateUpdate.Answer -> applyItems(it.items)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val step = taskViewModel.getStepByIndexAs<ChooseOneStep>(indexArg())

        step?.let {

            if (chooseOneStepViewModel.state.items.isEmpty())
                chooseOneStepViewModel.execute(ChooseOneStepStateEvent.Initialize(it, it.values))
            else
                applyItems(chooseOneStepViewModel.state.items)

            applyData(it)

        }
    }

    private fun applyData(step: ChooseOneStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        viewBinding?.button?.applyImageAsButton(step.buttonImage)
        viewBinding?.button?.setOnClickListener {
            startCoroutineAsync { next() }
        }

        viewBinding?.button?.setOnClickListener {

            val answer =
                chooseOneStepViewModel.getSelectedAnswer()

            answer?.let {

                addResult(
                    SingleAnswerResult(
                        step.identifier,
                        start,
                        ZonedDateTime.now(),
                        step.questionId,
                        AnswerResult(it.id, it.otherText)
                    )
                )

                checkSkip(step, answer)
            }

        }
    }

    private fun setupRecyclerView() {

        val viewBinding = binding

        viewBinding?.recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        viewBinding?.recyclerView?.adapter = adapter

        viewBinding?.recyclerView?.addItemDecoration(
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

    private fun applyItems(items: List<DroidItem<Any>>) {

        binding?.button?.isEnabled = chooseOneStepViewModel.getSelectedAnswer() != null

        adapter.submitList(items)

    }

    private fun checkSkip(step: ChooseOneStep, answerItem: ChooseOneAnswerItem) {

        val skip = step.skips.firstOrNull { it.answerId == answerItem.id }

        if (skip != null) skipTo(skip.target)
        else next()

    }

}