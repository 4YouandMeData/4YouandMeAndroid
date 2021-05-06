package com.foryouandme.ui.compose.topappbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun ForYouAndMeTopAppBar(
    imageConfiguration: ImageConfiguration,
    icon: TopAppBarIcon,
    modifier: Modifier =Modifier,
    title: String? = null,
    titleColor: Color? = null,
    onBack: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().then(modifier)
    ) {
        TopAppBar(
            backgroundColor = Color.Transparent,
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Image(
                        painter =
                        painterResource(
                            id =
                            when (icon) {
                                TopAppBarIcon.Back -> imageConfiguration.back()
                                TopAppBarIcon.BackSecondary -> imageConfiguration.backSecondary()
                                TopAppBarIcon.Close -> imageConfiguration.close()
                                TopAppBarIcon.CloseSecondary -> imageConfiguration.closeSecondary()
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        )
        if (title != null && titleColor != null)
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp, end = 60.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = titleColor
                )
            }
    }
}

sealed class TopAppBarIcon {

    object Back : TopAppBarIcon()
    object BackSecondary : TopAppBarIcon()
    object Close : TopAppBarIcon()
    object CloseSecondary : TopAppBarIcon()

}

@Preview
@Composable
private fun FourBooksTopAppBarPreview() {
    ForYouAndMeTheme {
        ForYouAndMeTopAppBar(
            icon = TopAppBarIcon.Back,
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}