package com.foryouandme.researchkit.step.introduction.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.databinding.StepIntroductionListBinding
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.researchkit.step.StepFragment
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionListStepFragment : StepFragment(R.layout.step_introduction_list) {

    private val adapter: DroidAdapter by lazy {

        DroidAdapter(
            IntroductionHeaderViewHolder.factory(),
            IntroductionViewHolder.factory()
        )

    }

    private val binding: StepIntroductionListBinding?
        get() = view?.let { StepIntroductionListBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        viewModel.getStepByIndexAs<IntroductionListStep>(indexArg())?.let { applyData(it) }

    }

    private fun setupView() {

        val viewBinding = binding

        viewBinding?.remindMeLater?.setOnClickListener { reschedule() }

        viewBinding?.action1?.setOnClickListener { next() }

    }

    private fun applyData(step: IntroductionListStep) {

        val viewBinding = binding

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.recyclerView?.layoutManager =
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )

        adapter.submitList(
            listOf(IntroductionHeaderItem(step.title, step.titleColor, step.image))
                .plus(step.list)
        )

        viewBinding?.recyclerView?.adapter = adapter

        viewBinding?.recyclerView?.addItemDecoration(
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

        viewBinding?.shadow?.background =
            HEXGradient.from(
                HEXColor.transparent(),
                HEXColor.parse(step.shadowColor)
            ).drawable(0.3f)

        viewBinding?.footer?.setBackgroundColor(step.footerBackgroundColor)

        viewBinding?.remindMeLater?.background = button(step.remindButtonColor)
        viewBinding?.remindMeLater?.text = step.remindButton(requireContext())
        viewBinding?.remindMeLater?.setTextColor(step.remindButtonTextColor)

        viewBinding?.action1?.background = button(step.buttonColor)
        viewBinding?.action1?.text = step.button
        viewBinding?.action1?.setTextColor(step.buttonTextColor)

    }

}