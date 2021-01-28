package com.foryouandme.core.researchkit.task

import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.web.CamCogInterface
import com.foryouandme.core.ext.web.IntegrationLoginInterface
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.camcog.CamCogTask

class FYAMCamCogTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val pages: List<Page>,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val reschedule: Reschedule?,
    url: String,
    cookies: Map<String, String>,
    camCogInterface: CamCogInterface,
) : Task(TaskIdentifiers.CAMCOG, id) {

    override val steps: List<Step> by lazy {

        val secondary =
            configuration.theme.secondaryColor.color()

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                getCamCogIntroStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO,
                index == 0 && reschedule.isEnabled()
            )

        }.plus(
            CamCogTask.buildCoreSteps(
                webBackgroundColor = secondary,
                webProgressBarColor = primaryEnd,
                webUrl = url,
                webCookies = cookies,
                webJavascriptInterface = camCogInterface,
                webJavascriptInterfaceName = IntegrationLoginInterface.INTEGRATION_NAME,
            )
        ).pipe { list ->

            successPage?.let {

                list.plus(
                    FYAMPageStep(
                        getCamCogSuccessStepId(it.id),
                        Back(imageConfiguration.backSecondary()),
                        configuration,
                        it,
                        EPageType.SUCCESS,
                        false
                    )
                )

            } ?: list

        }

    }

    private fun getCamCogIntroStepId(introId: String): String =
        "cam_cog_intro_${introId}"

    private fun getCamCogSuccessStepId(successId: String): String =
        "cam_cog_success_${successId}"

}