package org.fouryouandme.researchkit.task.gait

import com.squareup.moshi.Moshi
import org.fouryouandme.R
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.start.StartStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class GaitTask(
    id: String,
    startBackgroundColor: Int,
    startTitle: String?,
    startTitleColor: Int,
    startDescription: String?,
    startDescriptionColor: Int,
    startButton: String?,
    startButtonColor: Int,
    startButtonTextColor: Int,
    private val moshi: Moshi
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        listOf(
            StartStep(
                identifier = GAIT_START,
                backgroundColor = startBackgroundColor,
                title = { startTitle ?: it.getString(R.string.GAIT_title) },
                titleColor = startTitleColor,
                description = { startDescription ?: it.getString(R.string.GAIT_start) },
                descriptionColor = startDescriptionColor,
                button = { startButton ?: it.getString(R.string.GAIT_start) },
                buttonColor = startButtonColor,
                buttonTextColor = startButtonTextColor,
            )
        )

    }

    companion object {

        const val GAIT_START: String = "gait_start"

        const val GAIT_INTRO: String = "gait_intro"

        const val GAIT_COUNT_DOWN: String = "gait_count_down"

        const val GAIT_OUTBOUND: String = "gait_outbound"

        const val GAIT_RETURN: String = "gait_return"

        const val GAIT_REST: String = "gait_rest"

    }

}