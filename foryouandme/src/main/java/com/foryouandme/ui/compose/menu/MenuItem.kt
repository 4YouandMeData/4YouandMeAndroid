package com.foryouandme.ui.compose.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.toImageSource
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage

@Composable
fun MenuItem(
    text: String,
    icon: ImageSource,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        MultiSourceImage(
            source = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 24.dp, end = 16.dp)
        )
        Image(
            painter = painterResource(id = imageConfiguration.arrow()),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
private fun MenuItemPreview() {
    MenuItem(
        text = Mock.title,
        icon = R.drawable.placeholder.toImageSource(),
        configuration = Configuration.mock(),
        imageConfiguration = ImageConfiguration.mock()
    )
}