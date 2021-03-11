package com.foryouandme.researchkit.step.trailmaking

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.databinding.StepTrailMakingBinding
import com.foryouandme.entity.task.trailmaking.TrailMakingPoint
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
                    TrailMakingStateUpdate.CurrentIndex -> drawLine()
                    TrailMakingStateUpdate.SecondsElapsed,
                    TrailMakingStateUpdate.ErrorCount -> applyTimerErrorText()
                    TrailMakingStateUpdate.Completed ->
                        lifecycleScope.launchSafe {
                            delay(1000)
                            next()
                        }
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    is TrailMakingError.WrongPoint -> applyPointError(it.cause.point)
                }
            }
            .observeIn(this)

        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())

        if (viewModel.state.points.isEmpty() && step != null) {
            viewModel.execute(TrailMakingStateEvent.Initialize(step.type))
            viewModel.execute(TrailMakingStateEvent.StartTimer)
        }
        else {
            applyData()
            applyTimerErrorText()
        }

    }

    private fun applyData() {

        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())
        val viewBinding = binding

        if (viewBinding != null && step != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.timerAndErrorCount.setTextColor(step.timerAndErrorTextColor)

            viewBinding.title.text = step.titleText
            viewBinding.title.setTextColor(step.titleTextColor)

            viewBinding.pointsArea.setLineColor(step.lineColor)

            viewBinding.pointsArea.post {

                val screenPoints = getScreenPoints()

                screenPoints.forEach { position ->

                    val point = TrailMakingPointView(requireContext())
                    point.text = position.name
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
                        viewBinding.pointsArea.id,
                        ConstraintSet.TOP,
                        0
                    )
                    set.connect(
                        point.id,
                        ConstraintSet.START,
                        viewBinding.pointsArea.id,
                        ConstraintSet.START,
                        0
                    )
                    set.applyTo(viewBinding.root)

                    point.post {
                        point.translationX = position.x.toFloat() - (point.width / 2)
                        point.translationY = position.y.toFloat() - (point.width / 2)
                        point.elevation = 5.dpToPx().toFloat()
                        point.setOnClickListener {
                            viewModel.execute(TrailMakingStateEvent.SelectPoint(position))
                        }
                    }

                }

            }

        }

    }

    private fun drawLine() {

        val screenPoints = getScreenPoints()

        val currentPoint =
            screenPoints.getOrNull(viewModel.state.currentIndex)
        val previousPoint =
            screenPoints.getOrNull(viewModel.state.currentIndex - 1)
        val viewBinding = binding

        if (currentPoint != null && previousPoint != null && viewBinding != null) {
            clearErrors()
            viewBinding.pointsArea.addLine(currentPoint to previousPoint)
        }

    }

    private fun applyPointError(point: TrailMakingPoint) {

        clearErrors()

        val viewBinding = binding
        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())

        if (viewBinding != null && step != null) {

            val pointsViews =
                viewBinding.root.children.toList().filterIsInstance<TrailMakingPointView>()

            val pointView = pointsViews.firstOrNull { it.text == point.name }
            pointView?.setCircleBackgroundColor(step.pointErrorColor)

        }

    }

    private fun clearErrors() {

        val viewBinding = binding
        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())

        if (viewBinding != null && step != null) {

            viewBinding.root.children.toList()
                .filterIsInstance<TrailMakingPointView>()
                .forEach { it.setCircleBackgroundColor(step.pointColor) }

        }

    }

    private fun getScreenPoints(): List<TrailMakingPoint> {

        val viewBinding = binding

        return if (viewBinding != null)
            viewModel.state.points
                .map {

                    val width = viewBinding.pointsArea.width - 80.dpToPx()
                    val height = viewBinding.pointsArea.height - 80.dpToPx()
                    val x = (width * it.x) + 40.dpToPx()
                    val y = (height * it.y) + 40.dpToPx()
                    it.copy(x = x, y = y)

                }
        else
            emptyList()

    }

    private fun applyTimerErrorText() {

        val viewBinding = binding

        if (viewBinding != null) {

            val secondsElapsed = viewModel.state.secondsElapsed
            val errorCount = viewModel.state.errorCount
            val errors = getString(R.string.TRAIL_MAKING_errors)

            val timerError = "${secondsElapsed}s ($errorCount $errors)"

            viewBinding.timerAndErrorCount.text = timerError

        }

    }

}