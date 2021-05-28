package com.foryouandme.entity.source

import android.graphics.drawable.BitmapDrawable
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.foryouandme.core.ext.decodeBase64Image
import com.foryouandme.entity.configuration.button.button

sealed class ImageSource {

    data class AndroidResource(@DrawableRes val image: Int) : ImageSource() {

        companion object {

            fun Int.toAndroidResource(): AndroidResource = AndroidResource(this)

        }

    }

    data class Base64(val image: String) : ImageSource()

}

fun ImageView.applyImage(imageResource: ImageSource) {
    when (imageResource) {
        is ImageSource.AndroidResource -> setImageResource(imageResource.image)
        is ImageSource.Base64 -> {

            val bitmap = imageResource.image.decodeBase64Image()
            bitmap?.let { setImageBitmap(it) }

        }
    }
}

fun Button.applyImage(imageResource: ImageSource) {
    when (imageResource) {
        is ImageSource.AndroidResource -> setBackgroundResource(imageResource.image)
        is ImageSource.Base64 -> {

            val bitmap =
                imageResource.image
                    .decodeBase64Image()
                    ?.let { BitmapDrawable(context.resources, it) }

            bitmap?.let { background = it }

        }
    }
}

fun ImageView.applyImageAsButton(imageResource: ImageSource): Unit {
    when (imageResource) {
        is ImageSource.AndroidResource -> background =
            button(context.resources, imageResource.image)
        is ImageSource.Base64 -> {

            val bitmap =
                imageResource.image
                    .decodeBase64Image()
                    ?.let { BitmapDrawable(context.resources, it) }

            bitmap?.let { background = it }

        }
    }
}

@Composable
fun MultiSourceImage(
    source: ImageSource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    when (source) {
        is ImageSource.AndroidResource ->
            Image(
                painter = painterResource(id = source.image),
                contentDescription = contentDescription,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter
            )
        is ImageSource.Base64 -> {

            val bitmap =
                source
                    .image
                    .decodeBase64Image()

            if (bitmap != null)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = modifier,
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter = colorFilter
                )

        }
    }
}