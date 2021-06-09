package com.foryouandme.ui.compose.text

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.foryouandme.R
import com.foryouandme.core.ext.html.setHtmlText
import kotlin.math.roundToInt

@Composable
fun HtmlText(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {

    val lineHeight = LocalDensity.current.run { style.lineHeight.toPx() }

    AndroidView(
        factory = { TextView(it) },
        modifier = modifier,
        update = {

            it.setHtmlText(text)
            val red = color.red
            val green = color.green
            val blue = color.blue

            it.setTextColor(
                Color.rgb(
                    (red * 255).roundToInt(),
                    (green * 255).roundToInt(),
                    (blue * 255).roundToInt()
                )
            )

            it.typeface = ResourcesCompat.getFont(it.context, R.font.helvetica)
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.fontSize.value)
            val fontHeight: Int = it.paint.getFontMetricsInt(null)
            it.setLineSpacing(lineHeight - fontHeight, 1f)
            it.gravity =
                when(textAlign) {
                    TextAlign.Left -> Gravity.START
                    TextAlign.Right -> Gravity.END
                    TextAlign.Center -> Gravity.CENTER
                    TextAlign.Justify -> Gravity.START
                    TextAlign.Start -> Gravity.START
                    TextAlign.End -> Gravity.END
                    else -> Gravity.START
                }

        }
    )
}