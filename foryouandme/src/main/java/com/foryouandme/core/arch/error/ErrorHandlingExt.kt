package com.foryouandme.core.arch.error

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.navigation.AnywhereToAuth
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController

suspend fun <A> Either<ForYouAndMeError, A>.handleAuthError(
    controller: RootNavController,
    navigator: Navigator
): Either<ForYouAndMeError, A> =
    fold(
        { error ->

            when (error) {
                is ForYouAndMeError.NetworkErrorHTTP -> {
                    when (error.code) {

                        // 410, 401 error fond, the token is expired
                        // navigate to auth page
                        410,
                        401 ->
                            navigator.navigateTo(controller, AnywhereToAuth)
                    }
                }
                is ForYouAndMeError.UserNotLoggedIn ->
                    navigator.navigateTo(controller, AnywhereToAuth)
                is ForYouAndMeError.MissingConfiguration ->
                    navigator.navigateTo(controller, AnywhereToAuth)
            }

            error.left()

        },
        { it.right() }
    )