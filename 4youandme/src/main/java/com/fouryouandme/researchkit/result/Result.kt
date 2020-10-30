package com.fouryouandme.researchkit.result

import com.fouryouandme.researchkit.utils.ObjectUtils
import java.io.Serializable
import java.util.*

/**
 * The Result class defines the attributes of a result from one step or a group of steps.
 * Certain types of results can contain other results, which together express a hierarchy;
 * examples of these types of results are StepResult and TaskResult.
 *
 * Every object in the result hierarchy has an identifier that should correspond to the identifier
 * of an object in the original step hierarchy. Similarly, every object has a start date and an end
 * date that correspond to the range of times during which the result was collected. In an
 * StepResult object, for example, the start and end dates cover the range of time during which the
 * step view was visible on screen.
 *
 * When you implement a new type of step, it is usually helpful to create a new Result subclass to
 * hold the type of result data the step can generate, unless it makes sense to use an existing
 * subclass.
 *
 * Typically, Task and Step view instantiate result (and Result subclass) objects;
 * you seldom need to instantiate a result object in your code.
 *
 * @param identifier The unique identifier of the result.
 *
 * The identifier can be used to identify the question that was asked or the task that was
 * completed to produce the result. Typically, the identifier is copied from the originating
 * object.
 *
 * For example, a task result receives its identifier from a task, a step result receives its
 * identifier from a step
 *
 */
open class Result(var identifier: String) : Serializable {

    /**
     * The time when the task, step, or data collection began.
     */
    var startDate: Date? = null

    /**
     * The time when the task, step, or data collection stopped.
     */
    var endDate: Date? = null

    /**
     * @param newIdentifier new identifier for result
     * @return a deep copy of this object, and its polymorphism, with a new identifier set
     */
    fun deepCopy(newIdentifier: String): Result? {
        val copy = ObjectUtils.clone(this)
        copy?.identifier = newIdentifier
        return copy
    }

    override fun toString(): String {
        val sb = StringBuffer("Result{")
        sb.append("identifier='").append(identifier).append('\'')
        sb.append(", startDate=").append(startDate)
        sb.append(", endDate=").append(endDate)
        sb.append('}')
        return sb.toString()
    }

}