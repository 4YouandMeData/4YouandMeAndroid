package org.fouryouandme.core.entity.configuration

data class Text(
    val error: Error,
    val url: Url,
    val welcome: Welcome,
    val intro: Intro,
    val signUpLater: SignUpLater,
    val phoneVerification: PhoneVerification,
    val onboarding: Onboarding,
    val screening: Screening
)

data class Error(
    val titleDefault: String,
    val messageDefault: String,
    val buttonRetry: String,
    val buttonCancel: String,
    val messageRemoteServer: String,
    val messageConnectivity: String
)

data class Url(
    val privacy: String,
    val terms: String
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

data class Onboarding(
    val abortTitle: String,
    val abortButton: String,
    val abortCancel: String,
    val abortConfirm: String,
    val abortMessage: String,
    val agreeButton: String,
    val disagreeButton: String,
    val user: OnboardingUser
)

data class OnboardingUser(
    val nameTitle: String,
    val nameLastNameDescription: String,
    val nameFirstNameDescription: String,
    val signatureTitle: String,
    val signatureBody: String,
    val signatureClear: String,
    val signaturePlaceholder: String,
    val emailInfo: String,
    val emailDescription: String,
    val emailVerificationTitle: String,
    val emailVerificationBody: String,
    val emailVerificationCodeDescription: String,
    val emailVerificationResend: String,
    val emailVerificationWrongMail: String,
    val error: OnboardingUserError
)

data class OnboardingUserError(
    val emailVerificationWrongCode: String
)

data class Screening(
    val errorRetryButton: String
)