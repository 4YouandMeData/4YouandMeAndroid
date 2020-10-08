package org.fouryouandme.aboutyou.review

import arrow.core.toOption
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page

class AboutYouReviewConsentViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule,
    private val consentReviewModule: ConsentReviewModule
) : BaseViewModel<
        ForIO,
        AboutYouReviewConsentState,
        AboutYouReviewConsentStateUpdate,
        AboutYouReviewConsentError,
        AboutYouReviewConsentLoading>
    (
    navigator = navigator,
    runtime = runtime
) {

    /* --- data --- */

    suspend fun initialize(navController: RootNavController, configuration: Configuration): Unit {

        showLoadingFx(AboutYouReviewConsentLoading.Initialization)

        consentReviewModule.getConsent()
            .fold(
                { setErrorFx(it, AboutYouReviewConsentError.Initialization) },
                { consent ->

                    val items = mutableListOf<DroidItem<Any>>()

                    items.addAll(
                        consent.welcomePage
                            .toItems()
                            .map { it.toConsentReviewPageItem(configuration) }
                    )

                    setStateFx(
                        AboutYouReviewConsentState(consentReview = consent, items = items)
                    ) {
                        AboutYouReviewConsentStateUpdate.Initialization(
                            consent,
                            items
                        )
                    }
                }
            )

        hideLoadingFx(AboutYouReviewConsentLoading.Initialization)

    }


    private fun Page.toItems(): MutableList<Page> {

        var page = this.toOption()

        val items = mutableListOf(this)

        while (page.flatMap { it.link1 }.isDefined()) {

            val nextPage = page.flatMap { it.link1 }

            nextPage.map { items.add(it) }

            page = nextPage

        }

        return items

    }
}