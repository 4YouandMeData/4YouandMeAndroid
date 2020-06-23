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
import arrow.core.getOrElse
import arrow.core.some
import kotlinx.android.synthetic.main.loading.view.*
import org.fouryouandme.R
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.*

class LoadingView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var rotation: Option<Animation> = None

    init {

        View.inflate(context, R.layout.loading, this)

        rotation = AnimationUtils.loadAnimation(context, R.anim.rotate).some()

        applyTheme(true, null)

        isFocusable = true
        isClickable = true

        elevation = 20.dpToPx().toFloat()

        hide()

    }

    private fun show(): Unit {

        rotation.map { loader.startAnimation(it) }

        visibility = View.VISIBLE


    }

    private fun hide(): Unit {

        loader.clearAnimation()

        visibility = View.GONE
    }

    fun setVisibility(
        isVisible: Boolean,
        opaque: Boolean = true,
        @DrawableRes loaderImage: Int? = null
    ): Unit {

        applyTheme(opaque, loaderImage)

        if (isVisible) show() else hide()
    }

    private fun setLoader(@DrawableRes image: Int): Unit = loader.setImageResource(image)

    private fun applyTheme(opaque: Boolean, @DrawableRes loaderImage: Int?): Unit =
        context.IORuntime.fx.concurrent {

            continueOn(context.IORuntime.injector.runtimeContext.mainDispatcher)

            loader.setImageResource(loaderImage ?: context.imageConfiguration.loading())

            val configuration =
                ConfigurationUseCase.getConfiguration(context.IORuntime, CachePolicy.MemoryOrDisk)
                    .bind()
                    .toOption()


            val color =
                configuration
                    .map { it.theme.secondaryColor.color() }
                    .getOrElse { ContextCompat.getColor(context, R.color.loading) }

            continueOn(context.IORuntime.injector.runtimeContext.mainDispatcher)

            setBackgroundColor(if (opaque) adjustAlpha(color, 0.5f) else color)

        }.unsafeRunAsync()

}