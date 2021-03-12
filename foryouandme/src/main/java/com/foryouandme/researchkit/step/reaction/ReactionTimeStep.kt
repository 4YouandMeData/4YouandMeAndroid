package com.foryouandme.researchkit.step.reaction

import com.foryouandme.researchkit.step.Step

/**
 *
 * @param maximumStimulusIntervalSeconds:
 * maximum seconds (starting from the beginning of the step) within which to make the circle appear
 * @param minimumStimulusIntervalSeconds:
 * minimum seconds (starting from the beginning of the step) after which the circle will appear
 * @param timeoutSeconds:
 * seconds (starting from when the circle appears) beyond which the attempt is considered
 * unsuccessful and is repeated
 *
 */
class ReactionTimeStep(
    identifier: String,
    val backgroundColor: Int,
    val titleText: String?,
    val titleTextColor: Int,
    val attemptsTextColor: Int,
    val checkMarkBackgroundColor: Int,
    val checkMarkColor: Int,
    val maximumStimulusIntervalSeconds: Long,
    val minimumStimulusIntervalSeconds: Long,
    val numberOfAttempts: Int,
    val timeoutSeconds: Long
) : Step(identifier, null, null, { ReactionTimeStepFragment() })