package com.foryouandme.ui.main.feeds.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.foryouandme.core.arch.deps.logoStudySecondary
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.verticalGradient
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit
import java.text.MessageFormat

@Composable
fun FeedTopAppBar(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    user: User?,
    onLogoClicked: () -> Unit = {}
) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(configuration.theme.verticalGradient)
            .padding(horizontal = 16.dp)
    ) {
        if (user != null)
            Image(
                painter = painterResource(
                    id =
                    imageConfiguration.logoStudySecondary(
                        user,
                        configuration
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onLogoClicked() }
            )

        val title = getTitle(configuration, user)
        val subtitle = getSubtitle(configuration, user)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                if (title != null)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.body1,
                        color = configuration.theme.secondaryColor.value.copy(alpha = 0.5f)
                    )
                if (subtitle != null)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.h1,
                        color = configuration.theme.secondaryColor.value
                    )
            }
    }
}

private fun getTitle(configuration: Configuration, user: User?): String? =
    if (user?.getDeliveryEndDate(configuration) != null) configuration.text.tab.feedTitlePhase2
    else getFormattedPregnancyMonths(configuration, user)

private fun getSubtitle(configuration: Configuration, user: User?): String? =
    getFormattedDeliveryWeeks(configuration, user) ?: getFormattedPregnancyWeeks(configuration, user)

private fun getFormattedPregnancyMonths(configuration: Configuration, user: User?): String? {

    val pregnancyMonths = getPregnancyMonths(user)
    if (pregnancyMonths == null || pregnancyMonths < 0) return null
    return pregnancyMonths
        .let { "${(it / 3) + 1}nd" }
        .let { MessageFormat.format(configuration.text.tab.feedTitle, it) }

}

private fun getFormattedPregnancyWeeks(configuration: Configuration, user: User?): String? {

    val pregnancyWeeks = getPregnancyWeeks(user)
    if (pregnancyWeeks == null || pregnancyWeeks < 0) return null
    return pregnancyWeeks
        .let { MessageFormat.format(configuration.text.tab.feedSubTitle, it) }

}

private fun getFormattedDeliveryWeeks(configuration: Configuration, user: User?): String? {

    val deliveryWeeks = getDeliveryWeeks(user, configuration)
    if (deliveryWeeks == null || deliveryWeeks < 0) return null
    return deliveryWeeks
        .let { MessageFormat.format(configuration.text.tab.feedSubTitle, it) }

}

private fun getPregnancyMonths(user: User?): Long? =
    user?.getPregnancyEndDate()
        ?.getPregnancyStartDate()
        ?.differenceInMonthsFromNow()

private fun getPregnancyWeeks(user: User?): Long? =
    user?.getPregnancyEndDate()
        ?.getPregnancyStartDate()
        ?.differenceInWeeksFromNow()

private fun getDeliveryWeeks(user: User?, configuration: Configuration): Long? =
    user?.getDeliveryEndDate(configuration)
        ?.getPregnancyStartDate()
        ?.differenceInWeeksFromNow()


private fun User.getPregnancyEndDate(): LocalDate? =
    catchToNull {
        getCustomDataByIdentifier(UserCustomData.PREGNANCY_END_DATE_IDENTIFIER)
            ?.value
            ?.let { LocalDate.parse(it) }
    }

private fun User.getDeliveryEndDate(configuration: Configuration): LocalDate? =
    catchToNull {
        val userPhase = phase
        if (userPhase != null) {
            val index = configuration.text.phases.indexOf(userPhase.phase.name)
            if (index > 0) userPhase.end?.toLocalDate()
            else null
        } else null
    }

private fun LocalDate.getPregnancyStartDate(): LocalDate? =
    minusDays(280)

private fun LocalDate.differenceInMonthsFromNow(): Long =
    ChronoUnit.MONTHS.between(this, LocalDateTime.now().atZone(ZoneOffset.UTC))

private fun LocalDate.differenceInWeeksFromNow(): Long =
    ChronoUnit.WEEKS.between(this, LocalDateTime.now().atZone(ZoneOffset.UTC))

@Preview
@Composable
fun FeedTopAppBarPreview() {
    ForYouAndMeTheme {
        FeedTopAppBar(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            user = User.mock()
        )
    }
}