package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.Text

data class StringsResponse(
    @Json(name="INTRO_BODY") val introBody: String? = null,
    @Json(name="SETUP_ERROR_TITLE") val setupErrorTitle: String? = null,
    @Json(name="ERROR_TITLE_DEFAULT") val errorTitleDefault: String? = null,
    @Json(name="ERROR_MESSAGE_DEFAULT") val errorMessageDefault: String? = null,
    @Json(name="INTRO_BACK") val introBack: String? = null,
    @Json(name="GENERIC_INFO_TITLE") val genericInfoTitle: String? = null,
    @Json(name="INTRO_TITLE") val introTitle: String? = null,
    @Json(name="ERROR_BUTTON_RETRY") val errorButtonRetry: String? = null,
    @Json(name="ERROR_BUTTON_CANCEL") val errorButtonCancel: String? = null,
    @Json(name="WELCOME_START_BUTTON") val welcomeStartButton: String? = null,
    @Json(name="ERROR_MESSAGE_REMOTE_SERVER") val errorMessageRemoteButton: String? = null,
    @Json(name="INTRO_LOGIN") val introLogin: String? = null,
    @Json(name="ERROR_MESSAGE_CONNECTIVITY") val errorMessageConnectivity: String? = null,
    @Json(name="SETUP_LATER_BODY") val setupLaterBody: String? = null,
    @Json(name="SETUP_LATER_CONFIRM_BUTTON") val setupLaterConfirmButton: String? = null
) {

    fun toText(): Option<Text> =
        Option.fx {

            Text(
                !errorTitleDefault.toOption(),
                !errorMessageDefault.toOption(),
                !errorButtonRetry.toOption(),
                !errorButtonCancel.toOption(),
                !errorMessageRemoteButton.toOption(),
                !errorMessageConnectivity.toOption(),
                !setupErrorTitle.toOption(),
                !introTitle.toOption(),
                !introBody.toOption(),
                !introBack.toOption(),
                !introLogin.toOption(),
                !genericInfoTitle.toOption(),
                !welcomeStartButton.toOption(),
                !setupLaterBody.toOption(),
                !setupLaterConfirmButton.toOption()
            )
        }
}