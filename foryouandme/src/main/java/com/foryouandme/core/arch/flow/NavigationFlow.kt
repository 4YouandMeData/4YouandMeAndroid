package com.foryouandme.core.arch.flow

import com.foryouandme.core.arch.navigation.NavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class NavigationFlow @Inject constructor() {

    private val navigationFlow: MutableSharedFlow<UIEvent<NavigationAction>> = MutableSharedFlow()
    val navigation: SharedFlow<UIEvent<NavigationAction>>
        get() = navigationFlow


    suspend fun navigateTo(action: NavigationAction) {

        navigationFlow.emit(action.toUIEvent())

    }

}