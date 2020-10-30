package com.fouryouandme.aboutyou.review

import arrow.core.toOption
import com.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import com.fouryouandme.core.arch.error.handleAuthError
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.page.Page
import com.giacomoparisi.recyclerdroid.core.DroidItem

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