package com.foryouandme.ui.aboutyou.permissions.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.permission.Permission
import com.foryouandme.ui.compose.ForYouAndMeTheme

data class PermissionsItem(
    val configuration: Configuration,
    val id: String,
    val permission: Permission,
    val description: String,
    @DrawableRes val image: Int,
    val isAllowed: Boolean
)

@Composable
fun PermissionItem(
    item: PermissionsItem,
    configuration: Configuration,
    onItemClicked: (PermissionsItem) -> Unit = {}
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
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Divider(
                color = configuration.theme.secondaryColor.value,
                modifier =
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.body1,
                color = configuration.theme.secondaryColor.value,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = getGrantedStatus(
                    isAllowed = item.isAllowed,
                    configuration = configuration
                ),
                style = MaterialTheme.typography.h3,
                color =
                if (item.isAllowed) configuration.theme.primaryColorEnd.value
                else configuration.theme.secondaryColor.value,
                modifier =
                Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

private fun getGrantedStatus(isAllowed: Boolean, configuration: Configuration): AnnotatedString =
    buildAnnotatedString {
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(
                if (isAllowed) configuration.text.profile.permissionAllowed
                else configuration.text.profile.permissionAllow
            )
        }
    }

@Preview
@Composable
fun AboutYouPermissionPreview() {
    ForYouAndMeTheme {
        PermissionItem(
            item =
            PermissionsItem(
                configuration = Configuration.mock(),
                "",
                Permission.Location,
                Mock.body,
                R.drawable.placeholder,
                true
            ),
            configuration = Configuration.mock()
        )
    }
}