package com.foryouandme.ui.aboutyou.userInfo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun UserInfoEditSaveButton(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    isEditing: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Box(modifier = modifier.padding(end = 20.dp)) {
        Row(
            modifier =
            Modifier
                .border(
                    1.dp,
                    configuration.theme.secondaryColor.value,
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =
                if (isEditing) configuration.text.profile.submit
                else configuration.text.profile.edit,
                style = MaterialTheme.typography.h3,
                color = configuration.theme.secondaryColor.value
            )
            if (isEditing.not()) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = imageConfiguration.pencil()),
                    contentDescription = null,
                    modifier =
                    Modifier.size(10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserInfoEditButtonPreview() {
    ForYouAndMeTheme(Configuration.mock().toData()) {
        UserInfoEditSaveButton(
            configuration = it,
            imageConfiguration = ImageConfiguration.mock(),
            isEditing = false
        )
    }
}