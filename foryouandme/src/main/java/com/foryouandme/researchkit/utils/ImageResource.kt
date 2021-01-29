package com.foryouandme.researchkit.utils

import android.graphics.drawable.BitmapDrawable
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.foryouandme.core.ext.decodeBase64Image
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.entity.configuration.button.button

sealed class ImageResource {

    data class AndroidResource(@DrawableRes val image: Int) : ImageResource() {

        companion object {

            fun Int.toAndroidResource(): AndroidResource = AndroidResource(this)

        }

    }

    data class Base64(val image: String) : ImageResource()

}

fun ImageView.applyImage(imageResource: ImageResource) {
    when (imageResource) {
        is ImageResource.AndroidResource -> setImageResource(imageResource.image)
        is ImageResource.Base64 -> {

            val bitmap = imageResource.image.decodeBase64Image()
            bitmap?.let { setImageBitmap(it) }

        }
    }
}

fun Button.applyImage(imageResource: ImageResource): Unit =
    when (imageResource) {
        is ImageResource.AndroidResource -> setBackgroundResource(imageResource.image)
        is ImageResource.Base64 -> startCoroutineAsync {

            val bitmap =
                imageResource.image
                    .decodeBase64Image()
                    ?.let { BitmapDrawable(context.resources, it) }

            bitmap?.let { background = it }

        }
    }

fun ImageView.applyImageAsButton(imageResource: ImageResource): Unit {
    when (imageResource) {
        is ImageResource.AndroidResource -> background =
            button(context.resources, imageResource.image)
        is ImageResource.Base64 -> {

            val bitmap =
                imageResource.image
                    .decodeBase64Image()
                    ?.let { BitmapDrawable(context.resources, it) }

            bitmap?.let { background = it }

        }
    }
}