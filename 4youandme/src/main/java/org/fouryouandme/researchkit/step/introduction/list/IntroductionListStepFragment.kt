package org.fouryouandme.researchkit.step.introduction.list

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
            viewModel.getStepByIndexAs<IntroductionListStep>(indexArg())

        configuration {
            setupView()
            step?.let { applyData(it) }
        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {
            remind_me_later.setOnClickListener {
                startCoroutineAsync {
                    // TODO: call API to postpone task
                    startCoroutineAsync { viewModel.back(stepNavController(), taskNavController()) }
                }
            }

            next.setOnClickListener { startCoroutineAsync { next() } }
        }

    private suspend fun applyData(
        step: IntroductionListStep
    ): Unit =
        evalOnMain {

            root.setBackgroundColor(step.backgroundColor)

            recycler_view.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                )

            adapter.submitList(
                listOf(IntroductionHeaderItem(step.title, step.titleColor, step.image))
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
                    HEXColor.parse(step.shadowColor)
                ).drawable(0.3f)

            footer.setBackgroundColor(step.footerBackgroundColor)

            remind_me_later.background = button(step.remindButtonColor)
            remind_me_later.text = step.remindButton(requireContext())
            remind_me_later.setTextColor(step.remindButtonTextColor)

            next.background = button(step.buttonColor)
            next.text = step.button
            next.setTextColor(step.buttonTextColor)

            taskFragment().toolbar.apply {

                showCloseButton(imageConfiguration) {
                    startCoroutineAsync { viewModel.back(stepNavController(), taskNavController()) }
                }

                setBackgroundColor(step.toolbarColor)

                show()

            }

        }

}