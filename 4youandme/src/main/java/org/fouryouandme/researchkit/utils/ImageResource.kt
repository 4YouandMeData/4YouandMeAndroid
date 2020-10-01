package org.fouryouandme.researchkit.utils

import android.widget.Button
import android.widget.ImageView
import androidx.annotation.DrawableRes

sealed class ImageResource {

    data class AndroidResource(@DrawableRes val image: Int) : ImageResource() {

        companion object {

            fun Int.toAndroidResource(): AndroidResource = AndroidResource(this)

        }

    }

}

fun ImageView.applyImage(imageResource: ImageResource): Unit =
    when (imageResource) {
        is ImageResource.AndroidResource -> setImageResource(imageResource.image)
    }

fun Button.applyImage(imageResource: ImageResource): Unit =
    when (imageResource) {
        is ImageResource.AndroidResource -> setBackgroundResource(imageResource.image)
    }