package org.fouryouandme.researchkit.step.introduction

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import kotlinx.android.synthetic.main.step_introduction_list.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
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

        step.map { applyData(it) }
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

        adapter.submitList(step.list)
        recycler_view.adapter = adapter

        next.background = button(step.configuration.theme.primaryColorEnd.color())
        next.text = step.button
        next.setTextColor(step.configuration.theme.secondaryColor.color())
        next.setOnClickListener { next() }

    }

}