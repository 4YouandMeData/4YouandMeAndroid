package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon

@Composable
fun UserInfoHeader(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    isEditing: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditSaveClicked: () -> Unit = {}
) {

    Box(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ForYouAndMeTopAppBar(
                icon = TopAppBarIcon.Back,
                imageConfiguration = imageConfiguration,
                onBack = onBack
            )
            UserInfoEditSaveButton(
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                isEditing = isEditing,
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onEditSaveClicked
            )
        }
        Image(
            painter = painterResource(id = imageConfiguration.logoStudySecondary()),
            contentDescription = null,
            modifier = Modifier
                .width(65.dp)
                .align(Alignment.Center)
        )
        Text(
            text = configuration.text.profile.firstItem,
            color = configuration.theme.secondaryColor.value,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        )
    }
}

@Preview
@Composable
private fun UserInfoHeaderPreview() {
    ForYouAndMeTheme(Configuration.mock().toData()) {
        UserInfoHeader(
            configuration = it,
            imageConfiguration = ImageConfiguration.mock(),
            isEditing = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}