package com.foryouandme.researchkit.step.choosemany

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
import com.foryouandme.databinding.StepChooseManyBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.AnswerResult
import com.foryouandme.researchkit.result.MultipleAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.common.QuestionViewHolder
import com.foryouandme.entity.source.applyImageAsButton
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.ZonedDateTime

@AndroidEntryPoint
class ChooseManyStepFragment : StepFragment(R.layout.step_choose_many) {

    private val chooseManyStepViewModel: ChooseManyViewModel by viewModels()

    private val droidAdapter: DroidAdapter by lazy {
        DroidAdapter(
            QuestionViewHolder.factory(),
            ChooseManyAnswerViewHolder.factory(
                { chooseManyStepViewModel.execute(ChooseManyStepStateEvent.Answer(it.id)) },
                { item, text ->
                    chooseManyStepViewModel.execute(
                        ChooseManyStepStateEvent.AnswerTextChange(item.id, text)
                    )
                }
            )
        )
    }

    private val binding: StepChooseManyBinding?
        get() = view?.let { StepChooseManyBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseManyStepViewModel.stateUpdates
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ChooseManyStepStateUpdate.Initialization -> applyItems(it.items)
                    is ChooseManyStepStateUpdate.Answer -> applyItems(it.items)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val step =
            taskViewModel.getStepByIndexAs<ChooseManyStep>(indexArg())

        step?.let {

            applyData(it)

            if (chooseManyStepViewModel.state.items.isEmpty())
                chooseManyStepViewModel.execute(ChooseManyStepStateEvent.Initialize(it, it.values))
            else
                applyItems(chooseManyStepViewModel.state.items)

        }

    }

    private fun applyData(step: ChooseManyStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        viewBinding?.button?.applyImageAsButton(step.buttonImage)
        viewBinding?.button?.setOnClickListener {
            startCoroutineAsync { next() }
        }

        viewBinding?.button?.setOnClickListener {

            val answers =
                chooseManyStepViewModel.getSelectedAnswers()

            if (answers.isNotEmpty()) {

                addResult(
                    MultipleAnswerResult(
                        step.identifier,
                        start,
                        ZonedDateTime.now(),
                        step.questionId,
                        answers.map { AnswerResult(it.id, it.otherText) }
                    )
                )

                checkSkip(step, answers)

            }
        }
    }

    private fun setupRecyclerView() {

        binding?.recyclerView?.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


            adapter = droidAdapter


            addItemDecoration(
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

    }

    private fun applyItems(items: List<DroidItem<Any>>) {

        binding?.button?.isEnabled = chooseManyStepViewModel.getSelectedAnswers().isEmpty().not()

        droidAdapter.submitList(items)

    }

    private fun checkSkip(
        step: ChooseManyStep,
        answers: List<ChooseManyAnswerItem>
    ) {

        val answersId = answers.map { it.id }

        val skip = step.skips.firstOrNull { answersId.contains(it.answerId) }

        if (skip != null) skipTo(skip.target)
        else next()

    }

}