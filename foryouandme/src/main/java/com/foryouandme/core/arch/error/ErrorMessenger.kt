package com.foryouandme.core.arch.error

import android.content.Context
import com.foryouandme.R
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMessenger @Inject constructor(
    private val mapper: ErrorMapper,
    @ApplicationContext private val context: Context
) {

    fun getMessage(throwable: Throwable, configuration: Configuration?): String =
        when (mapper.mapToForYouAndMeException(throwable)) {
            is ForYouAndMeException.NetworkErrorUnknownHost ->
                context.getString(R.string.ERROR_network_connection)
            is ForYouAndMeException.NetworkErrorTimeOut ->
                context.getString(R.string.ERROR_network_connection)
            is ForYouAndMeException.NetworkHTTPException ->
                context.getString(R.string.ERROR_api)
            is ForYouAndMeException.MissingPhoneNumber ->
                configuration?.text?.phoneVerification?.error?.errorMissingNumber
                    ?: genericMessage()
            is ForYouAndMeException.WrongPhoneCode ->
                configuration?.text?.phoneVerification?.error?.errorWrongCode
                    ?: genericMessage()
            ForYouAndMeException.Unknown ->
                configuration?.text?.error?.messageDefault ?: genericMessage()
            ForYouAndMeException.MissingConfiguration,
            ForYouAndMeException.UserNotLoggedIn -> genericMessage()
        }

    fun getTitle(): String = context.getString(R.string.ERROR_title)

    private fun genericMessage(): String =
        context.getString(R.string.ERROR_generic)

}