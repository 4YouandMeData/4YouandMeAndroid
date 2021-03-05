package com.foryouandme.researchkit.step.trailmaking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.R
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TrailMakingViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<TrailMakingStateUpdate>,
    private val moshi: Moshi,
    @ApplicationContext private val application: Context
) : ViewModel() {

    /* --- state --- */

    var state: TrailMakingState = TrailMakingState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates

    /* --- initialize --- */

    private suspend fun initialize() {

        val pointsList = loadJson().parsePointsJson() ?: emptyList()

        val index = Random.nextInt(0, pointsList.size)

        val points = pointsList[index]

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

        state = state.copy(points = invertedPoints)
        stateUpdateFlow.update(TrailMakingStateUpdate.Initialized)

    }

    private fun loadJson(): String =
        application.resources.openRawResource(R.raw.trail_making_test_data)
            .bufferedReader()
            .use { it.readText() }

    private fun String.parsePointsJson(): List<List<TrailMakingPoint>>? {

        val trailMakingPointType = Types.newParameterizedType(
            MutableList::class.java,
            TrailMakingPoint::class.java
        )
        val trailMakingPointListType = Types.newParameterizedType(
            MutableList::class.java,
            trailMakingPointType
        )

        val trailMakingPointsAdapter: JsonAdapter<List<List<TrailMakingPoint>>> =
            moshi.adapter(trailMakingPointListType)

        return trailMakingPointsAdapter.fromJson(this)

    }

    /* --- state event --- */

    fun execute(stateEvent: TrailMakingStateEvent) {

        when (stateEvent) {
            TrailMakingStateEvent.Initialize ->
                viewModelScope.launchSafe { initialize() }
        }

    }

}