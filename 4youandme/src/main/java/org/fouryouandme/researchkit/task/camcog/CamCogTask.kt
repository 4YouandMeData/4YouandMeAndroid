package org.fouryouandme.researchkit.task.camcog

import org.fouryouandme.R
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.beforestart.WelcomeStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class CamCogTask(
    id: String,
    welcomeBackgroundColor: Int,
    welcomeImage: Int,
    welcomeTitle: String?,
    welcomeTitleColor: Int,
    welcomeDescription: String?,
    welcomeDescriptionColor: Int,
    welcomeRemindButton: String?,
    welcomeRemindButtonColor: Int,
    welcomeRemindButtonTextColor: Int,
    welcomeStartButton: String?,
    welcomeStartButtonColor: Int,
    welcomeStartButtonTextColor: Int,
    welcomeShadowColor: Int
) : Task(TaskIdentifiers.CAMCOG, id) {

    override val steps: List<Step> by lazy {

        listOf(
            WelcomeStep(
                identifier = CAMCOG_WELCOME,
                backgroundColor = welcomeBackgroundColor,
                image = welcomeImage,
                title = { welcomeTitle ?: it.getString(R.string.CAMCOG_welcome_title) },
                titleColor = welcomeTitleColor,
                description = { welcomeDescription ?: it.getString(R.string.CAMCOG_welcome_description) },
                descriptionColor = welcomeDescriptionColor,
                remindButton = { welcomeRemindButton ?: it.getString(R.string.TASK_welcome_remind_button) },
                remindButtonColor = welcomeRemindButtonColor,
                remindButtonTextColor = welcomeRemindButtonTextColor,
                startButton = { welcomeStartButton ?: it.getString(R.string.TASK_welcome_start_button) },
                startButtonColor = welcomeStartButtonColor,
                startButtonTextColor = welcomeStartButtonTextColor,
                shadowColor = welcomeShadowColor
            )
        )
    }

    companion object {

        const val CAMCOG_WELCOME: String = "camcog_welcome"

    }
}