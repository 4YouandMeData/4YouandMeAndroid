package org.fouryouandme.core.arch.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import kotlinx.android.synthetic.main.loading.view.*
import org.fouryouandme.R

class LoadingView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var rotation: Option<Animation> = None

    init {

        View.inflate(context, R.layout.loading, this)

        rotation = AnimationUtils.loadAnimation(context, R.anim.rotate).some()

        setBackgroundColor(ContextCompat.getColor(context, R.color.loading))

        isFocusable = true
        isClickable = true

        hide()

    }

    fun show(): Unit {

        rotation.map { loader.startAnimation(it) }

        visibility = View.VISIBLE


    }

    fun hide(): Unit {

        loader.clearAnimation()

        visibility = View.GONE
    }

    fun setVisibility(isVisible: Boolean): Unit =
        if (isVisible) show() else hide()


    fun setLoader(@DrawableRes image: Int): Unit = loader.setImageResource(image)

}