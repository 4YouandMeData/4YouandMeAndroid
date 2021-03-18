package com.foryouandme.ui.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.auth.IsLoggedUseCase
import com.foryouandme.domain.usecase.push.GetPushTokenUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.domain.usecase.user.UpdateUserTimeZoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.ZoneId
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<SplashLoading>,
    private val navigationFlow: NavigationFlow,
    private val getPushTokenUseCase: GetPushTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val isLoggedUseCase: IsLoggedUseCase,
    private val updateUserTimeZoneUseCase: UpdateUserTimeZoneUseCase
) : ViewModel() {

    /* --- flow --- */

    val loading = loadingFlow.loading
    val navigation = navigationFlow.navigation

    /* --- auth --- */

    private suspend fun auth() {

        loadingFlow.show(SplashLoading.Auth)

        // Token logging for debug
        val token = catchToNullSuspend { getPushTokenUseCase() } ?: "Error: can't load the token"
        Timber.tag("FCM_TOKEN").d(token)

        if (isLoggedUseCase()) {

            viewModelScope.launchSafe { updateUserTimeZoneUseCase(ZoneId.systemDefault()) }

            val user = catchToNullSuspend { getUserUseCase() }

            if (user == null)
                navigationFlow.navigateTo(SplashToWelcome)
            else {
                if (user.onBoardingCompleted) navigationFlow.navigateTo(SplashToMain)
                else navigationFlow.navigateTo(SplashToOnboarding)
            }

        } else navigationFlow.navigateTo(SplashToWelcome)

        loadingFlow.hide(SplashLoading.Auth)

    }

    /* --- state event --- */

    fun execute(stateEvent: SplashStateEvent) {

        when (stateEvent) {
            SplashStateEvent.Auth ->
                viewModelScope.launchSafe { auth() }
        }

    }

}