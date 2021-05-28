package com.foryouandme.data.datasource.network

import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.action.authActivityAction
import com.foryouandme.domain.error.ForYouAndMeException
import retrofit2.HttpException
import javax.inject.Inject

class AuthErrorInterceptor @Inject constructor(private val navigator: Navigator) {

    suspend fun <T> execute(block: suspend () -> T): T =

        try {
            block()
        } catch (error: Throwable) {

            when (error) {
                is HttpException -> {
                    when (error.code()) {

                        // 410, 401 error fond, the token is expired
                        // navigate to auth page
                        410,
                        401 ->
                            navigator.performAction(authActivityAction())
                    }
                }
                is ForYouAndMeException.UserNotLoggedIn ->
                    navigator.performAction(authActivityAction())
                is ForYouAndMeException.MissingConfiguration ->
                    navigator.performAction(authActivityAction())

            }

            throw error

        }

}