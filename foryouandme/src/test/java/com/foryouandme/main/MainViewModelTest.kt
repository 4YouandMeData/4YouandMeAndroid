package com.foryouandme.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.navigation.ActivityAction
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.openApp
import com.foryouandme.core.entity.integration.IntegrationApp
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private lateinit var navigator: Navigator

    private lateinit var viewModel: MainViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(): Unit {

        navigator = mockk()

        viewModel = spyk(MainViewModel(navigator))

        coEvery { navigator.navigateTo(allAny(), allAny()) } returns Unit
        coEvery { navigator.performAction(allAny()) } returns Unit

    }

    @Test
    fun testTaskDeepLinkNavigation(): Unit {

        val state = FYAMState(mockk(), "task_id".toEvent(), null, null)

        runBlocking { viewModel.handleDeepLink(mockk(), state) }

        coVerify { navigator.navigateTo(allAny(), match { it is MainToTask }) }

    }

    @Test
    fun testUrlDeepLinkNavigation(): Unit {

        val state = FYAMState(mockk(), null, "url".toEvent(), null)

        runBlocking { viewModel.handleDeepLink(mockk(), state) }

        coVerify { navigator.navigateTo(allAny(), match { it is AnywhereToWeb }) }

    }

    @Test
    fun testOpenAppIntegrationDeepLinkNavigation(): Unit {

        val state = FYAMState(mockk(), null, null, IntegrationApp.Oura.toEvent())

        val openAppActivityActionMock: ActivityAction = {}

        mockkStatic("com.foryouandme.core.arch.navigation.ActivityActionKt")

        every { openApp(IntegrationApp.Oura.packageName) } returns openAppActivityActionMock

        runBlocking { viewModel.handleDeepLink(mockk(), state) }

        coVerify { navigator.performAction(match { it == openAppActivityActionMock }) }

    }

}