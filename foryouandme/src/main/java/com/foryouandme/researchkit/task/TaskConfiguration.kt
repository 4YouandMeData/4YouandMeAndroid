package com.foryouandme.researchkit.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.researchkit.result.TaskResult

abstract class TaskConfiguration {

    abstract suspend fun build(id: String, data: Map<String, String>): Task?

    abstract suspend fun handleTaskResult(
        result: TaskResult,
        type: String,
        id: String
    ): TaskResponse

    abstract suspend fun reschedule(id: String): TaskResponse

    val taskResultLiveData: MutableLiveData<Event<TaskResponse>> = MutableLiveData()

}

sealed class TaskResponse {

    object Success : TaskResponse()

    data class Error(val message: (Context) -> String) : TaskResponse()

    fun <T> fold(
        error: (Error) -> T,
        success: () -> T
    ): T =
        when(this) {
            Success -> success()
            is Error -> error(this)
        }

    suspend fun <T> foldSuspend(
        error: suspend (Error) -> T,
        success: suspend () -> T
    ): T =
        when(this) {
            Success -> success()
            is Error -> error(this)
        }

}

