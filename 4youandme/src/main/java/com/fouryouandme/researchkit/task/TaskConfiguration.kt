package com.fouryouandme.researchkit.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.fouryouandme.core.arch.livedata.Event
import com.fouryouandme.researchkit.result.TaskResult

abstract class TaskConfiguration {

    abstract suspend fun build(type: String, id: String, data: Map<String, String>): Task?

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