package org.fouryouandme.aboutyou.review

import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page

class AboutYouReviewConsentViewModel(
    navigator: Navigator,
    private val consentReviewModule: ConsentReviewModule,
) : BaseViewModel<
        AboutYouReviewConsentState,
        AboutYouReviewConsentStateUpdate,
        AboutYouReviewConsentError,
        AboutYouReviewConsentLoading>
    (navigator) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoading(AboutYouReviewConsentLoading.Initialization)

        consentReviewModule.getConsent()
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setError(it, AboutYouReviewConsentError.Initialization) },
                { consent ->

                    val items = mutableListOf<DroidItem<Any>>()

                    items.addAll(
                        consent.welcomePage
                            .asList()
                            .map { it.toConsentReviewPageItem(configuration) }
                    )

                    setState(
                        AboutYouReviewConsentState(consentReview = consent, items = items)
                    ) {
                        AboutYouReviewConsentStateUpdate.Initialization(
                            consent,
                            items
                        )
                    }
                }
            )

        hideLoading(AboutYouReviewConsentLoading.Initialization)

    }


    private fun Page.toItems(): MutableList<Page> {

        var page = this.toOption()

        val items = mutableListOf(this)

        while (page.flatMap { it.link1.toOption() }.isDefined()) {

            val nextPage = page.flatMap { it.link1.toOption() }

            nextPage.map { items.add(it) }

            page = nextPage

        }

        return items

    }
}