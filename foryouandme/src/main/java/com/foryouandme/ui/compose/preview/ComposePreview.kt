package com.foryouandme.ui.compose.preview

import com.foryouandme.R
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.*

object ComposePreview {

    const val title: String = "Title Title Title Title Title Title"

    const val body: String =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In efficitur risus " +
                "accumsan, pulvinar sem sed, dignissim ipsum. Fusce rutrum purus elit, " +
                "nec lacinia elit bibendum vel. Suspendisse volutpat gravida urna, at " +
                "tincidunt ante tempus non. Nullam ac eleifend lacus. Sed massa libero, " +
                "rhoncus in enim quis, vestibulum rhoncus lorem. Vivamus mollis finibus " +
                "cursus. Donec sed tellus non justo venenatis fringilla vel vitae odio. " +
                "Donec in pulvinar leo, nec aliquet purus."

    const val bodyBig: String =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In efficitur risus " +
                "accumsan, pulvinar sem sed, dignissim ipsum. Fusce rutrum purus elit, " +
                "nec lacinia elit bibendum vel. Suspendisse volutpat gravida urna, at " +
                "tincidunt ante tempus non. Nullam ac eleifend lacus. Sed massa libero, " +
                "rhoncus in enim quis, vestibulum rhoncus lorem. Vivamus mollis finibus " +
                "cursus. Donec sed tellus non justo venenatis fringilla vel vitae odio. " +
                "Donec in pulvinar leo, nec aliquet purus. Aliquam risus magna, blandit " +
                "nec semper non, ultricies a enim. Sed nec gravida lacus. Class aptent " +
                "taciti sociosqu ad litora torquent per conubia nostra, per " +
                "inceptos himenaeos. Sed egestas, turpis ac suscipit egestas, " +
                "lectus felis rutrum eros, at venenatis tortor metus et diam. " +
                "Fusce tincidunt nisl a sem fermentum, nec molestie augue porttitor. " +
                "Integer hendrerit vel urna cursus pretium. Donec sodales, lacus quis " +
                "dignissim pharetra, lectus est dictum erat, quis pretium libero arcu id " +
                "nisi. Suspendisse eget lorem at tortor suscipit dapibus convallis id urna."

    const val name: String = "Name"

    const val button = "Button Button Button"

    const val time: String = "09:30"

    val configuration: LazyData.Data<Configuration> =
        Configuration(
            theme = themeMock,
            text = textMock,
            countryCodes = emptyList(),
            integrationsIdentifiers = emptyList(),
            pinCodeLogin = false
        ).toData()

    val imageConfiguration: ImageConfiguration = imageConfigurationMock

}

private val themeMock: Theme =
    Theme(
        primaryColorStart = HEXColor("#eb4034"),
        primaryColorEnd = HEXColor("#ed281a"),
        secondaryColor = HEXColor("#FFFFFF"),
        tertiaryColorStart = HEXColor("#FFFFFF"),
        tertiaryColorEnd = HEXColor("#FFFFFF"),
        primaryTextColor = HEXColor("#000000"),
        secondaryTextColor = HEXColor("#FFFFFF"),
        tertiaryTextColor = HEXColor("#FFFFFF"),
        fourthTextColor = HEXColor("#696666"),
        primaryMenuColor = HEXColor("#FFFFFF"),
        secondaryMenuColor = HEXColor("#FFFFFF"),
        activeColor = HEXColor("#FFFFFF"),
        deactiveColor = HEXColor("#FFFFFF"),
        fourthColor = HEXColor("#FFFFFF")
    )

private val textMock: Text =
    Text(
        error =
        Error(
            titleDefault = "",
            messageDefault = "",
            buttonRetry = "",
            buttonCancel = "",
            messageRemoteServer = "",
            messageConnectivity = ""
        ),
        url =
        Url(
            privacy = "",
            terms = ""
        ),
        welcome =
        Welcome(startButton = ""),
        intro =
        Intro(
            title = "",
            body = "",
            back = "",
            login = ""
        ),
        signUpLater =
        SignUpLater(
            body = "",
            confirmButton = ""
        ),
        phoneVerification =
        PhoneVerification(
            title = "",
            body = "",
            legal = "",
            numberDescription = "",
            legalPrivacyPolicy = "",
            legalTermsOfService = "",
            resendCode = "",
            wrongNumber = "",
            codeTitle = "",
            codeBody = "",
            codeDescription = "",
            error =
            PhoneVerificationError(
                errorMissingNumber = "",
                errorWrongCode = ""
            )
        ),
        onboarding =
        Onboarding(
            sections = emptyList(),
            introVideoContinueButton = "",
            abortTitle = "",
            abortButton = "",
            abortCancel = "",
            abortConfirm = "",
            abortMessage = "",
            agreeButton = "",
            disagreeButton = "",
            user =
            OnboardingUser(
                nameTitle = "",
                nameBody = "",
                nameLastNameDescription = "",
                nameFirstNameDescription = "",
                signatureTitle = "",
                signatureBody = "",
                signatureClear = "",
                signaturePlaceholder = "",
                emailInfo = "",
                emailDescription = "",
                emailVerificationTitle = "",
                emailVerificationBody = "",
                emailVerificationCodeDescription = "",
                emailVerificationResend = "",
                emailVerificationWrongMail = "",
                error =
                OnboardingUserError(
                    emailVerificationWrongCode = ""
                )
            ),
            optIn =
            OnboardingOptIn(
                submitButton = "",
                mandatoryClose = "",
                mandatoryTitle = "",
                mandatoryDefault = ""
            ),
            integration =
            OnboardingIntegration(
                downloadButtonDefault = "",
                openAppDefault = "",
                loginButtonDefault = "",
                nextDefault = ""
            )
        ),
        tab =
        Tab(
            feed = "",
            feedTitle = "",
            feedSubTitle = "",
            feedEmptyTitle = "",
            feedEmptySubTitle = "",
            feedHeaderTitle = "",
            feedHeaderSubTitle = "",
            feedHeaderPoints = "",
            tasks = "",
            tasksTitle = "",
            tabTaskEmptyTitle = "",
            tabTaskEmptySubtitle = "",
            tabTaskEmptyButton = "",
            userData = "",
            userDataTitle = "",
            studyInfo = "",
            studyInfoTitle = ""
        ),
        activity =
        Activity(
            activityButtonDefault = "",
            quickActivityButtonDefault = "",
            quickActivityButtonNext = "",
            quickActivitiesTotalNumber = ""
        ),
        feed =
        Feed(
            educationalButtonDefault = "",
            rewardButtonDefault = "",
            alertButtonDefault = ""
        ),
        videoDiary =
        VideoDiary(
            introTitle = "",
            introButton = "",
            introParagraphTitleA = "",
            introParagraphBodyA = "",
            introParagraphTitleB = "",
            introParagraphBodyB = "",
            introParagraphTitleC = "",
            introParagraphBodyC = "",
            recorderInfoTitle = "",
            recorderInfoBody = "",
            recorderTitle = "",
            recorderCloseButton = "",
            recorderReviewButton = "",
            recorderSubmitButton = "",
            successTitle = "",
            discardTitle = "",
            discardBody = "",
            discardCancel = "",
            discardConfirm = "",
            missingPermissionDiscard = "",
            missingPermissionTitleMic = "",
            missingPermissionBodyMic = "",
            missingPermissionTitleCamera = "",
            missingPermissionBodyCamera = "",
            missingPermissionBodySettings = "",
            recorderStartRecordingDescription = "",
            recorderResumeRecordingDescription = "",
        ),
        studyInfo =
        StudyInfo(
            aboutYou = "",
            contactInfo = "",
            rewards = "",
            faq = "",
        ),
        profile =
        Profile(
            title = ComposePreview.title,
            firstItem = "",
            secondItem = "",
            thirdItem = "",
            fourthItem = "",
            fifthItem = "",
            disclaimer = "",
            connect = "",
            deauthorize = "",
            permissionAllow = "",
            permissionAllowed = "",
            permissionDenied = "",
            permissionCancel = "",
            permissionMessage = "",
            permissionSettings = "",
            edit = "",
            submit = "",
            dailySurveyTimeDescription = "",
            dailySurveyTimingHidden = 0
        ),
        yourData =
        YourData(
            dataPeriodTitle = "",
            periodDay = "",
            periodWeek = "",
            periodMonth = "",
            periodYear = ""
        ),
        task =
        Task(
            remindButton = "",
            startButton = "",
            skipButton = "",
        ),
        gaitActivity =
        GaitActivity(
            introTitle = "",
            introBody = "",
        ),
        fitnessActivity =
        FitnessActivity(
            introTitle = "",
            introBody = ""
        ),
        camCogActivity =
        CamCogActivity(
            introTitle = "",
            introBody = "",
        ),
        survey =
        Survey(
            otherAnswerPlaceholder = ""
        )
    )

private val imageConfigurationMock: ImageConfiguration =
    object: ImageConfiguration {
        override fun splashLogo(): Int = R.drawable.error
        override fun pushSmallIcon(): Int = R.drawable.error
        override fun loading(): Int = R.drawable.error
        override fun back(): Int = R.drawable.error
        override fun backSecondary(): Int = R.drawable.error
        override fun close(): Int = R.drawable.error
        override fun closeSecondary(): Int = R.drawable.error
        override fun clear(): Int = R.drawable.error
        override fun logo(): Int = R.drawable.error
        override fun logoStudy(): Int = R.drawable.error
        override fun logoStudySecondary(): Int = R.drawable.error
        override fun nextStep(): Int = R.drawable.error
        override fun nextStepSecondary(): Int = R.drawable.error
        override fun previousStepSecondary(): Int = R.drawable.error
        override fun entryWrong(): Int = R.drawable.error
        override fun entryValid(): Int = R.drawable.error
        override fun tabFeed(): Int = R.drawable.error
        override fun tabTask(): Int = R.drawable.error
        override fun tabUserData(): Int = R.drawable.error
        override fun tabStudyInfo(): Int = R.drawable.error
        override fun timer(): Int = R.drawable.error
        override fun pocket(): Int = R.drawable.error
        override fun videoDiaryIntro(): Int = R.drawable.error
        override fun videoDiaryTime(): Int = R.drawable.error
        override fun videoDiaryClose(): Int = R.drawable.error
        override fun videoDiaryRecord(): Int = R.drawable.error
        override fun videoDiaryPause(): Int = R.drawable.error
        override fun videoDiaryPlay(): Int = R.drawable.error
        override fun videoDiaryFlashOn(): Int = R.drawable.error
        override fun videoDiaryFlashOff(): Int = R.drawable.error
        override fun videoDiaryToggleCamera(): Int = R.drawable.error
        override fun heartBeat(): Int = R.drawable.error
        override fun sittingMan(): Int = R.drawable.error
        override fun walkingMan(): Int = R.drawable.error
        override fun phoneShake(): Int = R.drawable.error
        override fun phoneShakeCircle(): Int = R.drawable.error
        override fun trailMaking(): Int = R.drawable.error
        override fun aboutYou(): Int = R.drawable.error
        override fun contactInfo(): Int = R.drawable.error
        override fun rewards(): Int = R.drawable.error
        override fun faq(): Int = R.drawable.error
        override fun arrow(): Int = R.drawable.error
        override fun pregnancy(): Int = R.drawable.error
        override fun devices(): Int = R.drawable.error
        override fun reviewConsent(): Int = R.drawable.error
        override fun permissions(): Int = R.drawable.error
        override fun dailySurveyTime(): Int = R.drawable.error
        override fun smartwatch(): Int = R.drawable.error
        override fun oura(): Int = R.drawable.error
        override fun instagram(): Int = R.drawable.error
        override fun rescuetime(): Int = R.drawable.error
        override fun twitter(): Int = R.drawable.error
        override fun garmin(): Int = R.drawable.error
        override fun deactivatedButton(): Int = R.drawable.error
        override fun location(): Int = R.drawable.error
        override fun pushNotification(): Int = R.drawable.error
        override fun editContainer(): Int = R.drawable.error
        override fun pencil(): Int = R.drawable.error
    }