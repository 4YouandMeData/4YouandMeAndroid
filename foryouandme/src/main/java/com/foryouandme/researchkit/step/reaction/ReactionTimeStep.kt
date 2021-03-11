package com.foryouandme.researchkit.step.reaction

import com.foryouandme.researchkit.step.Step

/**
 *
 * @param maximumStimulusIntervalSeconds:
 * maximum seconds (starting from the beginning of the step) within which to make the circle appear
 * @param minimumStimulusIntervalSeconds:
 * minimum seconds (starting from the beginning of the step) after which the circle will appear
 * @param thresholdAcceleration:
 * Acceleration threshold beyond which the reaction took place
 * @param timeoutSeconds:
 * seconds (starting from when the circle appears) beyond which the attempt is considered
 * unsuccessful and is repeated
 *
 */
class ReactionTimeStep(
    identifier: String,
    val maximumStimulusIntervalSeconds: Double,
    val minimumStimulusIntervalSeconds: Double,
    val thresholdAcceleration: Double,
    val numberOfAttempts: Int,
    val timeoutSeconds: Double
) : Step(identifier, null, null, { ReactionTimeStepFragment() })