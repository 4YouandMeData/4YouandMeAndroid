package com.foryouandme.core.arch.loading

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.adjustAlpha
import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoadingView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    @Inject
    lateinit var imageConfiguration: ImageConfiguration

    @Inject
    lateinit var getConfigurationUseCase: GetConfigurationUseCase

    private var rotation: Animation? = null

    init {

        View.inflate(context, R.layout.loading, this)

        rotation = AnimationUtils.loadAnimation(context, R.anim.rotate)

        applyTheme(true, null)

        isFocusable = true
        isClickable = true

        elevation = 20.dpToPx().toFloat()

        hide()

    }

    private fun show(): Unit {

        rotation?.let { loader.startAnimation(it) }

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
    ) {

        applyTheme(opaque, loaderImage)

        if (isVisible) show() else hide()
    }

    private fun setLoader(@DrawableRes image: Int): Unit = loader.setImageResource(image)

    @DelicateCoroutinesApi
    private fun applyTheme(opaque: Boolean, @DrawableRes loaderImage: Int?) {
        GlobalScope.launchSafe {

            withContext(Dispatchers.Main) {
                loader.setImageResource(loaderImage ?: R.drawable.loading)

                val configuration =
                    catchToNullSuspend { getConfigurationUseCase(Policy.LocalFirst) }

                val color =
                    configuration?.theme?.secondaryColor?.color()
                        ?: ContextCompat.getColor(context, R.color.loading)

                setBackgroundColor(if (opaque) adjustAlpha(color, 0.5f) else color)
            }

        }
    }

}