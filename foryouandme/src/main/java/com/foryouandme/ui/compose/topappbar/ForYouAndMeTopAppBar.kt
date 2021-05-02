package com.foryouandme.ui.compose.topappbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun ForYouAndMeTopAppBar(
    icon: TopAppBarIcon,
    onBack: () -> Unit = {}
) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
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
                    contentDescription = "",
                )
            }
        },
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    )
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
        ForYouAndMeTopAppBar(icon = TopAppBarIcon.Back)
    }
}