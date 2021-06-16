package com.foryouandme.entity.configuration

import com.foryouandme.entity.mock.Mock
import com.squareup.moshi.Json

data class Text(
    val error: Error,
    val url: Url,
    val welcome: Welcome,
    val intro: Intro,
    val signUpLater: SignUpLater,
    val phoneVerification: PhoneVerification,
    val onboarding: Onboarding,
    val tab: Tab,
    val activity: Activity,
    val feed: Feed,
    val videoDiary: VideoDiary,
    val studyInfo: StudyInfo,
    val profile: Profile,
    val yourData: YourData,
    val task: Task,
    val gaitActivity: GaitActivity,
    val fitnessActivity: FitnessActivity,
    val camCogActivity: CamCogActivity,
    val survey: Survey
) {

    companion object {

        fun mock(): Text =
            Text(
                error = Error.mock(),
                url = Url.mock(),
                welcome = Welcome.mock(),
                intro = Intro.mock(),
                signUpLater = SignUpLater.mock(),
                phoneVerification = PhoneVerification.mock(),
                onboarding = Onboarding.mock(),
                tab = Tab.mock(),
                activity = Activity.mock(),
                feed = Feed.mock(),
                videoDiary = VideoDiary.mock(),
                studyInfo = StudyInfo.mock(),
                profile = Profile.mock(),
                yourData = YourData.mock(),
                task = Task.mock(),
                gaitActivity = GaitActivity.mock(),
                fitnessActivity = FitnessActivity.mock(),
                camCogActivity = CamCogActivity.mock(),
                survey = Survey.mock()
            )

    }

}

data class Error(
    val titleDefault: String,
    val messageDefault: String,
    val buttonRetry: String,
    val buttonCancel: String,
    val messageRemoteServer: String,
    val messageConnectivity: String
) {

    companion object {

        fun mock(): Error =
            Error(
                titleDefault = Mock.title,
                messageDefault = Mock.body,
                buttonRetry = Mock.button,
                buttonCancel = Mock.button,
                messageRemoteServer = Mock.body,
                messageConnectivity = Mock.body
            )

    }

}

data class Url(
    val privacy: String,
    val terms: String
) {

    companion object {

        fun mock(): Url =
            Url(
                privacy = "",
                terms = ""
            )

    }

}

data class Welcome(
    val startButton: String
) {

    companion object {

        fun mock(): Welcome =
            Welcome(startButton = Mock.button)

    }

}

data class Intro(
    val title: String,
    val body: String,
    val back: String,
    val login: String
) {

    companion object {

        fun mock(): Intro =
            Intro(
                title = Mock.title,
                body = Mock.body,
                back = Mock.button,
                login = Mock.button
            )

    }

}

data class SignUpLater(
    val body: String,
    val confirmButton: String
) {

    companion object {

        fun mock(): SignUpLater =
            SignUpLater(
                body = Mock.body,
                confirmButton = Mock.button
            )

    }

}

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
) {

    companion object {

        fun mock(): PhoneVerification =
            PhoneVerification(
                title = Mock.title,
                body = Mock.body,
                legal = Mock.body,
                numberDescription = Mock.body,
                legalPrivacyPolicy = Mock.body,
                legalTermsOfService = Mock.body,
                resendCode = Mock.button,
                wrongNumber = Mock.body,
                codeTitle = Mock.title,
                codeBody = Mock.body,
                codeDescription = Mock.body,
                error = PhoneVerificationError.mock()
            )

    }

}

data class PhoneVerificationError(
    val errorMissingNumber: String,
    val errorWrongCode: String
) {

    companion object {

        fun mock(): PhoneVerificationError =
            PhoneVerificationError(
                errorMissingNumber = Mock.body,
                errorWrongCode = Mock.body
            )

    }

}

data class Onboarding(
    val sections: List<String>,
    val introVideoContinueButton: String,
    val abortTitle: String,
    val abortButton: String,
    val abortCancel: String,
    val abortConfirm: String,
    val abortMessage: String,
    val agreeButton: String,
    val disagreeButton: String,
    val user: OnboardingUser,
    val optIn: OnboardingOptIn,
    val integration: OnboardingIntegration
) {

    companion object {

        fun mock(): Onboarding =
            Onboarding(
                sections = emptyList(),
                introVideoContinueButton = Mock.button,
                abortTitle = Mock.title,
                abortButton = Mock.button,
                abortCancel = Mock.button,
                abortConfirm = Mock.button,
                abortMessage = Mock.body,
                agreeButton = Mock.button,
                disagreeButton = Mock.button,
                user = OnboardingUser.mock(),
                optIn = OnboardingOptIn.mock(),
                integration = OnboardingIntegration.mock()
            )

    }

}

data class OnboardingUser(
    val nameTitle: String,
    val nameBody: String,
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
) {

    companion object {

        fun mock(): OnboardingUser =
            OnboardingUser(
                nameTitle = Mock.title,
                nameBody = Mock.body,
                nameLastNameDescription = Mock.body,
                nameFirstNameDescription = Mock.body,
                signatureTitle = Mock.title,
                signatureBody = Mock.body,
                signatureClear = Mock.button,
                signaturePlaceholder = Mock.body,
                emailInfo = Mock.body,
                emailDescription = Mock.body,
                emailVerificationTitle = Mock.title,
                emailVerificationBody = Mock.body,
                emailVerificationCodeDescription = Mock.body,
                emailVerificationResend = Mock.button,
                emailVerificationWrongMail = Mock.body,
                error = OnboardingUserError.mock(),
            )

    }

}

data class OnboardingOptIn(
    val submitButton: String,
    val mandatoryClose: String,
    val mandatoryTitle: String,
    val mandatoryDefault: String
) {

    companion object {

        fun mock(): OnboardingOptIn =
            OnboardingOptIn(
                submitButton = Mock.button,
                mandatoryClose = Mock.button,
                mandatoryTitle = Mock.title,
                mandatoryDefault = Mock.body
            )

    }

}

data class OnboardingIntegration(
    val downloadButtonDefault: String,
    val openAppDefault: String,
    val loginButtonDefault: String,
    val nextDefault: String
) {

    companion object {

        fun mock(): OnboardingIntegration =
            OnboardingIntegration(
                downloadButtonDefault = Mock.button,
                openAppDefault = Mock.button,
                loginButtonDefault = Mock.button,
                nextDefault = Mock.button
            )

    }

}

data class OnboardingUserError(
    val emailVerificationWrongCode: String
) {

    companion object {

        fun mock(): OnboardingUserError =
            OnboardingUserError(
                emailVerificationWrongCode = Mock.body
            )

    }

}

data class Tab(
    val feed: String,
    val feedTitle: String,
    val feedSubTitle: String,
    val feedEmptyTitle: String,
    val feedEmptySubTitle: String,
    val feedHeaderTitle: String,
    val feedHeaderSubTitle: String,
    val feedHeaderPoints: String,
    val tasks: String,
    val tasksTitle: String,
    val tabTaskEmptyTitle: String,
    val tabTaskEmptySubtitle: String,
    val tabTaskEmptyButton: String,
    val userData: String,
    val userDataTitle: String,
    val studyInfo: String,
    val studyInfoTitle: String
) {

    companion object {

        fun mock(): Tab =
            Tab(
                feed = Mock.body,
                feedTitle = Mock.title,
                feedSubTitle = Mock.title,
                feedEmptyTitle = Mock.title,
                feedEmptySubTitle = Mock.title,
                feedHeaderTitle = Mock.title,
                feedHeaderSubTitle = Mock.title,
                feedHeaderPoints = Mock.title,
                tasks = Mock.button,
                tasksTitle = Mock.title,
                tabTaskEmptyTitle = Mock.title,
                tabTaskEmptySubtitle = Mock.title,
                tabTaskEmptyButton = Mock.button,
                userData = Mock.button,
                userDataTitle = Mock.title,
                studyInfo = Mock.button,
                studyInfoTitle = Mock.title
            )

    }

}

data class Activity(
    val activityButtonDefault: String,
    val quickActivityButtonDefault: String,
    val quickActivityButtonNext: String,
    val quickActivitiesTotalNumber: String
) {

    companion object {

        fun mock(): Activity =
            Activity(
                activityButtonDefault = Mock.button,
                quickActivityButtonDefault = Mock.button,
                quickActivityButtonNext = Mock.button,
                quickActivitiesTotalNumber = Mock.button
            )

    }

}

data class Feed(
    val educationalButtonDefault: String,
    val rewardButtonDefault: String,
    val alertButtonDefault: String
) {

    companion object {

        fun mock(): Feed =
            Feed(
                educationalButtonDefault = Mock.button,
                rewardButtonDefault = Mock.button,
                alertButtonDefault = Mock.button
            )

    }

}

data class VideoDiary(
    val introTitle: String,
    val introButton: String,
    val introParagraphTitleA: String,
    val introParagraphBodyA: String,
    val introParagraphTitleB: String,
    val introParagraphBodyB: String,
    val introParagraphTitleC: String,
    val introParagraphBodyC: String,
    val recorderInfoTitle: String,
    val recorderInfoBody: String,
    val recorderTitle: String,
    val recorderCloseButton: String,
    val recorderReviewButton: String,
    val recorderSubmitButton: String,
    val successTitle: String,
    val discardTitle: String,
    val discardBody: String,
    val discardCancel: String,
    val discardConfirm: String,
    val missingPermissionDiscard: String,
    val missingPermissionTitleMic: String,
    val missingPermissionBodyMic: String,
    val missingPermissionTitleCamera: String,
    val missingPermissionBodyCamera: String,
    val missingPermissionBodySettings: String,
    val recorderStartRecordingDescription: String,
    val recorderResumeRecordingDescription: String,
) {

    companion object {

        fun mock(): VideoDiary =
            VideoDiary(
                introTitle = Mock.title,
                introButton = Mock.button,
                introParagraphTitleA = Mock.title,
                introParagraphBodyA = Mock.body,
                introParagraphTitleB = Mock.title,
                introParagraphBodyB = Mock.button,
                introParagraphTitleC = Mock.title,
                introParagraphBodyC = Mock.body,
                recorderInfoTitle = Mock.title,
                recorderInfoBody = Mock.body,
                recorderTitle = Mock.title,
                recorderCloseButton = Mock.button,
                recorderReviewButton = Mock.button,
                recorderSubmitButton = Mock.button,
                successTitle = Mock.title,
                discardTitle = Mock.title,
                discardBody = Mock.body,
                discardCancel = Mock.button,
                discardConfirm = Mock.button,
                missingPermissionDiscard = Mock.button,
                missingPermissionTitleMic = Mock.title,
                missingPermissionBodyMic = Mock.body,
                missingPermissionTitleCamera = Mock.title,
                missingPermissionBodyCamera = Mock.body,
                missingPermissionBodySettings = Mock.button,
                recorderStartRecordingDescription = Mock.body,
                recorderResumeRecordingDescription = Mock.body,
            )

    }

}

data class StudyInfo(
    val aboutYou: String,
    val contactInfo: String,
    val rewards: String,
    val faq: String,
) {

    companion object {

        fun mock(): StudyInfo =
            StudyInfo(
                aboutYou = Mock.button,
                contactInfo = Mock.button,
                rewards = Mock.button,
                faq = Mock.button,
            )

    }

}

data class Profile(
    val title: String,
    val firstItem: String,
    val secondItem: String,
    val thirdItem: String,
    val fourthItem: String,
    val fifthItem: String,
    val disclaimer: String,
    val connect: String,
    val deauthorize: String,
    val permissionAllow: String,
    val permissionAllowed: String,
    val permissionDenied: String,
    val permissionCancel: String,
    val permissionMessage: String,
    val permissionSettings: String,
    val permissionLocation: String,
    val edit: String,
    val submit: String,
    val dailySurveyTimeDescription: String,
    val dailySurveyTimingHidden: Int
) {

    companion object {

        fun mock(): Profile =
            Profile(
                title = Mock.title,
                firstItem = Mock.title,
                secondItem = Mock.title,
                thirdItem = Mock.title,
                fourthItem = Mock.title,
                fifthItem = Mock.title,
                disclaimer = Mock.body,
                connect = Mock.button,
                deauthorize = Mock.button,
                permissionAllow = Mock.button,
                permissionAllowed = Mock.button,
                permissionDenied = Mock.button,
                permissionCancel = Mock.button,
                permissionMessage = Mock.body,
                permissionSettings = Mock.button,
                permissionLocation = Mock.body,
                edit = Mock.button,
                submit = Mock.button,
                dailySurveyTimeDescription = Mock.body,
                dailySurveyTimingHidden = 0
            )

    }

}

data class YourData(
    val dataPeriodTitle: String,
    val periodDay: String,
    val periodWeek: String,
    val periodMonth: String,
    val periodYear: String,
    val emptyFilterButton: String,
    val emptyFilterMessage: String,
    val filterTitle: String,
    val filterClearButton: String,
    val filterSelectAllButton: String,
    val filterSaveButton: String,
) {

    companion object {

        fun mock(): YourData =
            YourData(
                dataPeriodTitle = Mock.title,
                periodDay = Mock.button,
                periodWeek = Mock.button,
                periodMonth = Mock.button,
                periodYear = Mock.button,
                emptyFilterButton = Mock.button,
                emptyFilterMessage = Mock.body,
                filterTitle = Mock.title,
                filterClearButton = Mock.button,
                filterSelectAllButton = Mock.button,
                filterSaveButton = Mock.button
            )

    }

}

data class Task(
    val remindButton: String,
    val startButton: String,
    val skipButton: String,
) {

    companion object {

        fun mock(): Task =
            Task(
                remindButton = Mock.button,
                startButton = Mock.button,
                skipButton = Mock.button,
            )

    }

}

data class GaitActivity(
    val introTitle: String,
    val introBody: String,
) {

    companion object {

        fun mock(): GaitActivity =
            GaitActivity(
                introTitle = Mock.title,
                introBody = Mock.body,
            )

    }

}

data class FitnessActivity(
    val introTitle: String,
    val introBody: String
) {

    companion object {

        fun mock(): FitnessActivity =
            FitnessActivity(
                introTitle = Mock.title,
                introBody = Mock.body,
            )

    }

}

data class CamCogActivity(
    val introTitle: String,
    val introBody: String,
) {

    companion object {

        fun mock(): CamCogActivity =
            CamCogActivity(
                introTitle = Mock.title,
                introBody = Mock.body,
            )

    }

}

data class Survey(
    val otherAnswerPlaceholder: String
) {

    companion object {

        fun mock(): Survey =
            Survey(otherAnswerPlaceholder = Mock.body)

    }

}
