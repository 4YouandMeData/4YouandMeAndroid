package org.fouryouandme.researchkit.step.chooseone

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.step_choose_one.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.fouryouandme.researchkit.utils.applyImageAsButton
import org.threeten.bp.ZonedDateTime

class ChooseOneStepFragment : StepFragment(R.layout.step_choose_one) {

    private val chooseOneStepViewModel: ChooseOneViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                ChooseOneViewModel(
                    navigator,
                    IORuntime
                )
            }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(ChooseOneAnswerViewHolder.factory {

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
                    chooseOneStepViewModel.initialize(it.values)

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

            step.image?.let { icon.applyImage(it) }
            icon.isVisible = step.image != null

            question.text = step.question(requireContext())
            question.setTextColor(step.questionColor)

            shadow.background = shadow(step.shadowColor)

            button.applyImageAsButton(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync { next() }
            }

            button.setOnClickListener {
                startCoroutineAsync {
                    //TODO: add answer id to the response
                    viewModel.addResult(
                        SingleAnswerResult(
                            step.identifier,
                            start,
                            ZonedDateTime.now(),
                            step.questionId,
                            chooseOneStepViewModel.getSelectedAnswer()?.text ?: ""
                        )
                    )
                    next()
                }
            }
        }

    private suspend fun setupRecyclerView() =

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

}