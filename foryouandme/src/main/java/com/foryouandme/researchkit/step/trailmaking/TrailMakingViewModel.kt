package com.foryouandme.researchkit.step.trailmaking

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.R
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.ext.getTimestampDateUTC
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.task.result.trailmaking.Point
import com.foryouandme.entity.task.result.trailmaking.TrailMakingPoint
import com.foryouandme.entity.task.result.trailmaking.TrailMakingTap
import com.foryouandme.researchkit.step.trailmaking.ETrailMakingType.NUMBER
import com.foryouandme.researchkit.step.trailmaking.ETrailMakingType.NUMBER_AND_LETTER
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class TrailMakingViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<TrailMakingStateUpdate>,
    private val errorFlow: ErrorFlow<TrailMakingError>,
    private val moshi: Moshi,
    @ApplicationContext private val application: Context
) : ViewModel() {

    /* --- state --- */

    var state: TrailMakingState = TrailMakingState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val error = errorFlow.error

    /* --- initialize --- */

    private suspend fun initialize(type: ETrailMakingType) {

        val pointsList = loadJson().parsePointsJson() ?: emptyList()

        val randomIndex = Random.nextInt(0, pointsList.size)

        val points = pointsList[randomIndex]

        val invertX = Random.nextBoolean()
        val invertY = Random.nextBoolean()
        val invertAll = Random.nextBoolean()

        val invertedPoints =
            points
                .map {
                    it.copy(
                        x = if (invertX) 1 - it.x else it.x,
                        y = if (invertY) 1 - it.y else it.y
                    )
                }
                .map {
                    if (invertAll) it.copy(x = 1 - it.x, y = it.y)
                    else it
                }

        val numbers =
            listOf(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15
            )

        val letters =
            listOf(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "L", "M", "N", "O", "P"
            )

        val trailMakingPoint =
            when (type) {
                NUMBER -> {

                    val size = (invertedPoints.size.toFloat() / 2).roundToInt()

                    invertedPoints.take(size).mapIndexed { index, point ->
                        TrailMakingPoint(point.x, point.y, (index + 1).toString())
                    }

                }
                NUMBER_AND_LETTER -> {

                    var numberIndex = 0
                    var letterIndex = 0

                    invertedPoints.mapIndexed { index, value ->

                        val name =
                            if (index % 2 != 0) letters[letterIndex].also { letterIndex++ }
                            else numbers[numberIndex].toString().also { numberIndex++ }

                        TrailMakingPoint(value.x, value.y, name)
                    }
                }
            }

        state = state.copy(points = trailMakingPoint)
        stateUpdateFlow.update(TrailMakingStateUpdate.Initialized)

    }

    private fun loadJson(): String =
        application.resources.openRawResource(R.raw.trail_making_test_data)
            .bufferedReader()
            .use { it.readText() }

    private fun String.parsePointsJson(): List<List<Point>>? {

        val trailMakingPointType = Types.newParameterizedType(
            MutableList::class.java,
            Point::class.java
        )
        val trailMakingPointListType = Types.newParameterizedType(
            MutableList::class.java,
            trailMakingPointType
        )

        val trailMakingPointsAdapter: JsonAdapter<List<List<Point>>> =
            moshi.adapter(trailMakingPointListType)

        return trailMakingPointsAdapter.fromJson(this)

    }

    /* --- point --- */

    private suspend fun selectPoint(point: TrailMakingPoint) {

        val selectedIndex =
            state.points
                .indexOfFirst { it.name == point.name }
                .let { if (it < 0) null else it }

        if (selectedIndex != null) {

            val correct = selectedIndex == (state.currentIndex + 1)

            val tap = TrailMakingTap(selectedIndex, getTimestampDateUTC().time, correct.not())
            val taps = state.taps.plus(tap)
            state = state.copy(taps = taps)

            if (correct) {
                // correct point selected
                state = state.copy(currentIndex = selectedIndex)
                stateUpdateFlow.update(TrailMakingStateUpdate.CurrentIndex)
                if(selectedIndex == state.points.lastIndex)
                    stateUpdateFlow.update(TrailMakingStateUpdate.Completed)
            } else {
                // wrong point selected
                state = state.copy(errorCount = state.errorCount + 1)
                stateUpdateFlow.update(TrailMakingStateUpdate.ErrorCount)
                throw ForYouAndMeException.Unknown
            }

        }

    }

    /* --- timer --- */

    private suspend fun startTimer() {

        var secondsElapsed = 0L

        while (true) {
            delay(1000)
            secondsElapsed += 1
            state = state.copy(secondsElapsed = secondsElapsed)
            stateUpdateFlow.update(TrailMakingStateUpdate.SecondsElapsed)
        }

    }

    /* --- state event --- */

    fun execute(stateEvent: TrailMakingStateEvent) {

        when (stateEvent) {
            is TrailMakingStateEvent.Initialize ->
                viewModelScope.launchSafe { initialize(stateEvent.type) }
            is TrailMakingStateEvent.SelectPoint ->
                errorFlow.launchCatch(
                    viewModelScope,
                    TrailMakingError.WrongPoint(stateEvent.point)
                ) { selectPoint(stateEvent.point) }
            TrailMakingStateEvent.StartTimer ->
                viewModelScope.launchSafe { startTimer() }
        }

    }

}