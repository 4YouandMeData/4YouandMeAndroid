package org.fouryouandme.core.researchkit.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.web.CamCogInterface
import org.fouryouandme.core.ext.web.IntegrationLoginInterface
import org.fouryouandme.researchkit.task.camcog.CamCogTask

suspend fun FYAMTaskConfiguration.buildCamCog(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    url: String,
    cookies: Map<String, String>,
    camCogInterface: CamCogInterface
): CamCogTask {

    val secondary =
        configuration.theme.secondaryColor.color()

    val primaryText =
        configuration.theme.primaryTextColor.color()

    val primaryEnd =
        configuration.theme.primaryColorEnd.color()

    return CamCogTask(
        id = id,
        welcomeBackImage = imageConfiguration.backSecondary(),
        welcomeBackgroundColor = secondary,
        welcomeImage = imageConfiguration.videoDiaryIntro(),
        welcomeTitle = configuration.text.camCogActivity.introTitle,
        welcomeTitleColor = primaryText,
        welcomeDescription = configuration.text.camCogActivity.introBody,
        welcomeDescriptionColor = primaryText,
        welcomeRemindButton = configuration.text.task.remindButton,
        welcomeRemindButtonColor = secondary,
        welcomeRemindButtonTextColor = primaryEnd,
        welcomeStartButton = configuration.text.task.startButton,
        welcomeStartButtonColor = primaryEnd,
        welcomeStartButtonTextColor = secondary,
        welcomeShadowColor = primaryText,
        webBackgroundColor = secondary,
        webProgressBarColor = primaryEnd,
        webUrl = url,
        webCookies = cookies,
        webJavascriptInterface = camCogInterface,
        webJavascriptInterfaceName = IntegrationLoginInterface.INTEGRATION_NAME
    )

}