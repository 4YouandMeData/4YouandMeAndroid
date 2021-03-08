package com.foryouandme.researchkit.step.trailmaking

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.databinding.StepTrailMakingBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class TrailMakingStepFragment : StepFragment(R.layout.step_trail_making) {

    private val viewModel: TrailMakingViewModel by viewModels()

    private val binding: StepTrailMakingBinding?
        get() = view?.let { StepTrailMakingBinding.bind(it) }

    private val pointsId: MutableList<Int> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    TrailMakingStateUpdate.Initialized -> applyData()
                }
            }
            .observeIn(this)

        if (viewModel.state.points.isEmpty())
            viewModel.execute(TrailMakingStateEvent.Initialize)
        else
            applyData()

    }

    private fun applyData() {

        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())
        val viewBinding = binding

        if (viewBinding != null && step != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.pointsArea.post {

                val screenPoints = viewModel.state.points
                    .map {

                        val width = viewBinding.pointsArea.width - 80.dpToPx()
                        val height = viewBinding.pointsArea.height - 80.dpToPx()
                        val x = (width * it.x) + 40.dpToPx()
                        val y = (height * it.y) + 40.dpToPx()
                        it.copy(x = x, y = y)

                    }

                screenPoints.forEachIndexed { index, position ->

                    val point = TrailMakingPointView(requireContext())
                    point.setText(index.toString())
                    point.setCircleBackgroundColor(step.pointColor)
                    point.setTextColor(step.pointTextColor)
                    val id = View.generateViewId()
                    pointsId.add(id)
                    point.id = id

                    viewBinding.root.addView(point, 0)

                    val set = ConstraintSet()
                    set.clone(viewBinding.root)
                    set.connect(
                        point.id,
                        ConstraintSet.TOP,
                        viewBinding.root.id,
                        ConstraintSet.TOP,
                        60
                    )
                    set.applyTo(viewBinding.root)

                    point.translationX = position.x.toFloat()
                    point.translationY = position.y.toFloat()

                }

                screenPoints.forEachIndexed { index, point ->

                    val nextPoint = screenPoints.getOrNull(index + 1)

                    if (nextPoint != null) {

                        val line = TrailMakingLineView(requireContext())
                        line.setPoints(point, nextPoint)
                        val id = View.generateViewId()
                        line.id = id

                        viewBinding.root.addView(line, 0)

                        val set = ConstraintSet()
                        set.clone(viewBinding.root)
                        set.connect(
                            line.id,
                            ConstraintSet.TOP,
                            viewBinding.root.id,
                            ConstraintSet.TOP,
                            60
                        )
                        set.applyTo(viewBinding.root)

                    }
                }
            }

        }

    }

}