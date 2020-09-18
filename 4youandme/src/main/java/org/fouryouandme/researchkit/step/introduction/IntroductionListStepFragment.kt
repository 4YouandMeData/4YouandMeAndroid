package org.fouryouandme.researchkit.step.introduction

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.step_introduction_list.*
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment

class IntroductionListStepFragment : StepFragment(R.layout.step_introduction_list) {

    private val adapter: DroidAdapter by lazy {

        DroidAdapter(
            IntroductionHeaderViewHolder.factory(),
            IntroductionViewHolder.factory()
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.IntroductionListStep>(indexArg())

        step?.let { applyData(it) }

    }

    private fun applyData(
        step: Step.IntroductionListStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        recycler_view.layoutManager =
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )

        adapter.submitList(
            listOf(IntroductionHeaderItem(step.title, step.image, step.configuration))
                .plus(step.list)
        )

        recycler_view.adapter = adapter

        recycler_view.addItemDecoration(
            LinearMarginItemDecoration(
                topMargin = {
                    if (it.index == 0) 70.dpToPx()
                    else 45.dpToPx()
                },
                startMargin = { 25.dpToPx() },
                endMargin = { 25.dpToPx() },
                bottomMargin = {
                    if (it.index == it.itemCount - 1) 30.dpToPx()
                    else 0
                }
            )
        )

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                step.configuration.theme.primaryTextColor
            ).drawable(0.3f)

        footer.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        next.background = button(step.configuration.theme.primaryColorEnd.color())
        next.text = step.button
        next.setTextColor(step.configuration.theme.secondaryColor.color())
        next.setOnClickListener { startCoroutineAsync { next() } }

        taskFragment().toolbar.apply {

            showCloseButton(imageConfiguration) {
                startCoroutineAsync { viewModel.back(stepNavController(), taskNavController()) }
            }

            setBackgroundColor(step.configuration.theme.secondaryColor.color())

            show()

        }

    }

}