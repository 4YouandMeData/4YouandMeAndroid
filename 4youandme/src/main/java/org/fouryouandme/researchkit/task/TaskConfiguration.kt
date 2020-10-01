package org.fouryouandme.researchkit.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.researchkit.result.TaskResult

abstract class TaskConfiguration {

    abstract suspend fun build(type: String, id: String): Task?

    abstract suspend fun handleTaskResult(
        result: TaskResult,
        type: String,
        id: String
    ): TaskHandleResult

    val taskResultLiveData: MutableLiveData<Event<TaskHandleResult>> = MutableLiveData()

}

sealed class TaskHandleResult {

    object Handled : TaskHandleResult()

    data class Error(val message: (Context) -> String) : TaskHandleResult()

}