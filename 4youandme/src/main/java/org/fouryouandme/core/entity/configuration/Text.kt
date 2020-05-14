package org.fouryouandme.core.entity.configuration

data class Text(
    val error: Error,
    val welcome: Welcome,
    val intro: Intro,
    val signUpLater: SignUpLater,
    val phoneVerification: PhoneVerification
)

data class Error(
    val titleDefault: String,
    val messageDefault: String,
    val buttonRetry: String,
    val buttonCancel: String,
    val messageRemoteButton: String,
    val messageConnectivity: String
)

data class Welcome(
    val startButton: String
)

data class Intro(
    val title: String,
    val body: String,
    val back: String,
    val login: String
)

data class SignUpLater(
    val body: String,
    val confirmButton: String
)

data class PhoneVerification(
    val title: String,
    val body: String,
    val legal: String,
    val numberDescription: String,
    val legalPrivacyPolicy: String,
    val legalTermsOfService: String,
    val resendCode: String,
    val wrongNumber: String,
    val codeTitle: String,
    val codeBody: String,
    val codeDescription: String,
    val error: PhoneVerificationError
)

data class PhoneVerificationError(
    val errorMissingNumber: String,
    val errorWrongCode: String
)