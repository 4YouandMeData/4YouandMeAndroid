package com.foryouandme.ui.aboutyou.appsanddevices.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme

data class AppsAndDeviceItem(
    val name: String,
    @DrawableRes val image: Int,
    val isConnected: Boolean,
    val connectLink: String,
    val disconnectLink: String
) {

    companion object {

        fun mock(): AppsAndDeviceItem =
            AppsAndDeviceItem(
                name = Mock.name,
                image = R.drawable.placeholder,
                isConnected = true,
                connectLink = "connect_link",
                disconnectLink = "disconnect_link"
            )

    }

}

@Composable
fun AppsAndDeviceItem(
    item: AppsAndDeviceItem,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onItemClicked: (AppsAndDeviceItem) -> Unit = {}
) {

    Card(
        backgroundColor = configuration.theme.primaryColorStart.value,
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Spacer(
                modifier =
                Modifier
                    .width(1.dp)
                    .background(configuration.theme.secondaryColor.value)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.body1,
                color = configuration.theme.secondaryColor.value,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text =
                if (item.isConnected) configuration.text.profile.deauthorize
                else configuration.text.profile.connect,
                style = MaterialTheme.typography.h3,
                color =
                if (item.isConnected) configuration.theme.deactiveColor.value
                else configuration.theme.secondaryColor.value,
                modifier =
                Modifier.padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter =
                painterResource(
                    id =
                    if (item.isConnected) imageConfiguration.deactivatedButton()
                    else imageConfiguration.nextStep()
                ),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview
@Composable
fun AppAndDeviceItemPreview() {
    ForYouAndMeTheme {
        AppsAndDeviceItem(
            item = AppsAndDeviceItem.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}