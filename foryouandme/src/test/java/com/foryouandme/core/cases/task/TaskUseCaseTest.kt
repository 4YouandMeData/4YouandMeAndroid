package com.foryouandme.core.cases.task

import arrow.core.right
import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.ErrorModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.task.TaskRepository.reschedule
import com.foryouandme.core.cases.task.TaskUseCase.reschedule
import com.foryouandme.core.data.api.task.TaskApi
import com.squareup.moshi.Moshi
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TaskUseCaseTest {

    private lateinit var api: TaskApi
    private lateinit var moshi: Moshi
    private lateinit var environment: Environment
    private lateinit var errorModule: ErrorModule
    private lateinit var authModule: AuthModule

    private lateinit var taskModule: TaskModule

    @Before
    fun setUp(): Unit {

        api = mockk()
        moshi = mockk()
        environment = mockk()
        errorModule = mockk()
        authModule = mockk()

        taskModule =
            spyk(
                TaskModule(
                    api,
                    moshi,
                    environment,
                    errorModule,
                    authModule
                )
            )

        mockkObject(AuthUseCase)

        coEvery { authModule.getToken(allAny()) } returns "token".right()

    }

    @Test
    fun testReschedule(): Unit {

        mockkObject(TaskRepository)

        coEvery { api.reschedule(allAny(), allAny()) } returns Unit

        runBlocking { taskModule.reschedule("task_id") }


        coVerify { taskModule.reschedule(match { it == "token" }, match { it == "task_id" }) }

        coVerify { api.reschedule(match { it == "token" }, match { it == "task_id" }) }

    }

}