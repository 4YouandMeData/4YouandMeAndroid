package com.foryouandme.ui.userInfo.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.errorToast
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.userInfo.UserInfoAction.*
import com.foryouandme.ui.userInfo.UserInfoEvent
import com.foryouandme.ui.userInfo.UserInfoState
import com.foryouandme.ui.userInfo.UserInfoViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalDate

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun UserInfoPage(
    userInfoViewModel: UserInfoViewModel = viewModel(),
    onBack: () -> Unit = {},
    onFaqClicked: () -> Unit
) {

    val state by userInfoViewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = userInfoViewModel) {
        userInfoViewModel.events
            .onEach {
                when (it) {
                    UserInfoEvent.UploadCompleted ->
                        onBack()
                    is UserInfoEvent.UploadError ->
                        context.errorToast(it.error, state.configuration.dataOrNull())
                }
            }
            .collect()
    }

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { userInfoViewModel.execute(GetConfiguration) }
    ) {
        UserInfoPage(
            state = state,
            configuration = it,
            imageConfiguration = userInfoViewModel.imageConfiguration,
            onBack = onBack,
            onUserError = { userInfoViewModel.execute(GetUser) },
            onEditSaveClicked = { userInfoViewModel.execute(ToggleEditMode) },
            onTextChanged =
            { item, value -> userInfoViewModel.execute(OnTextChanged(item, value)) },
            onDateChanged =
            { item, value -> userInfoViewModel.execute(OnDateChanged(item, value)) },
            onValueSelected =
            { item, value -> userInfoViewModel.execute(OnPickerChanged(item, value)) },
            onPhaseInfoPositiveClicked = onFaqClicked,
            onPhaseInfoNegativeClicked = onBack,
            onPhaseSwitchConfirmClicked = { userInfoViewModel.execute(PhaseSwitch) },
            onPhaseSwitchCancelClicked = { userInfoViewModel.execute(AbortPhaseSwitch) }
        )
    }

}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun UserInfoPage(
    state: UserInfoState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit,
    onUserError: () -> Unit,
    onEditSaveClicked: () -> Unit,
    onTextChanged: (EntryItem.Text, String) -> Unit,
    onDateChanged: (EntryItem.Date, LocalDate) -> Unit,
    onValueSelected: (EntryItem.Picker, EntryItem.Picker.Value) -> Unit,
    onPhaseInfoPositiveClicked: () -> Unit,
    onPhaseInfoNegativeClicked: () -> Unit,
    onPhaseSwitchConfirmClicked: () -> Unit,
    onPhaseSwitchCancelClicked: () -> Unit
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    LoadingError(
        data = state.user,
        configuration = configuration,
        onRetryClicked = onUserError
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {
                UserInfoHeader(
                    user = it,
                    configuration = configuration,
                    imageConfiguration = imageConfiguration,
                    isEditing = state.isEditing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .background(configuration.theme.primaryColorStart.value),
                    onBack = onBack,
                    onEditSaveClicked = onEditSaveClicked
                )
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(state.entries) {
                        when (it) {
                            is EntryItem.Text ->
                                EntryTextItem(
                                    item = it,
                                    isEditable = state.isEditing,
                                    configuration = configuration,
                                    imageConfiguration = imageConfiguration,
                                    onTextChanged = onTextChanged
                                )
                            is EntryItem.Date ->
                                EntryDateItem(
                                    item = it,
                                    isEditable = state.isEditing && it.isEditable,
                                    configuration = configuration,
                                    imageConfiguration = imageConfiguration,
                                    onDateSelected = onDateChanged
                                )
                            is EntryItem.Picker ->
                                EntryPickerItem(
                                    item = it,
                                    isEditable = state.isEditing,
                                    configuration = configuration,
                                    imageConfiguration = imageConfiguration,
                                    onValueSelected = onValueSelected
                                )
                        }
                    }

                }
            }

            // PHASE SWITCHED INFO
            if (state.phaseAlert)
                PhaseSwitchedInfo(
                    configuration = configuration,
                    onPositiveClicked = onPhaseInfoPositiveClicked,
                    onNegativeClicked = onPhaseInfoNegativeClicked
                )

            // PHASE SWITCH ALERT
            if (state.pendingPhaseSwitch != null)
                PhaseSwitchAlert(
                    configuration = configuration,
                    onPositiveClicked = onPhaseSwitchConfirmClicked,
                    onNegativeClicked = onPhaseSwitchCancelClicked
                )

            Loading(
                configuration = configuration,
                isVisible = state.upload.isLoading(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}