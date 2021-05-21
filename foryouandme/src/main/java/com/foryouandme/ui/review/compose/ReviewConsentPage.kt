package com.foryouandme.ui.review.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.items.consent.ConsentReviewPageItem
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.review.ReviewConsentAction.GetConfiguration
import com.foryouandme.ui.review.ReviewConsentAction.GetReviewConsent
import com.foryouandme.ui.review.ReviewConsentState
import com.foryouandme.ui.review.ReviewConsentViewModel

@Composable
fun ReviewConsentPage(
    reviewConsentViewModel: ReviewConsentViewModel = viewModel(),
    onBack: () -> Unit = {},
) {

    val state by reviewConsentViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { reviewConsentViewModel.execute(GetConfiguration) }
    ) {
        ReviewConsentPage(
            state = state,
            configuration = it,
            imageConfiguration = reviewConsentViewModel.imageConfiguration,
            onBack = onBack,
            onConsentReviewError = { reviewConsentViewModel.execute(GetReviewConsent) }
        )
    }

}

@Composable
fun ReviewConsentPage(
    state: ReviewConsentState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
    onConsentReviewError: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    LoadingError(
        data = state.consentReview,
        configuration = configuration,
        onRetryClicked = onConsentReviewError
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ForYouAndMeTopAppBar(
                imageConfiguration = imageConfiguration,
                icon = TopAppBarIcon.Back,
                onBack = onBack,
                modifier = Modifier.background(configuration.theme.verticalGradient)
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {

                item {
                    Text(
                        text = configuration.text.profile.thirdItem,
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                items(state.items) {
                    ConsentReviewPageItem(
                        item = it,
                        configuration = configuration
                    )
                }
            }
        }
    }
}