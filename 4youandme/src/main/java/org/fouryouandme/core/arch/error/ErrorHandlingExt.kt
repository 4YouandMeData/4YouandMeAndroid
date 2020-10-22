package org.fouryouandme.core.arch.error

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.navigation.AnywhereToAuth
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController

suspend fun <A> Either<FourYouAndMeError, A>.handleAuthError(
    controller: RootNavController,
    navigator: Navigator
): Either<FourYouAndMeError, A> =
    fold(
        { error ->

            when (error) {
                is FourYouAndMeError.NetworkErrorHTTP -> {
                    when (error.code) {

                        // 410, 401 error fond, the token is expired
                        // navigate to auth page
                        410,
                        401 ->
                            navigator.navigateTo(controller, AnywhereToAuth)
                    }
                }
                is FourYouAndMeError.UserNotLoggedIn ->
                    navigator.navigateTo(controller, AnywhereToAuth)
                is FourYouAndMeError.MissingConfiguration ->
                    navigator.navigateTo(controller, AnywhereToAuth)
            }

            error.left()

        },
        { it.right() }
    )