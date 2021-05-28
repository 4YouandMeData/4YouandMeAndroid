package com.foryouandme.core.arch.error

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.foryouandme.R
import com.foryouandme.core.ext.dpToPx
import kotlinx.android.synthetic.main.error.view.*

class ErrorView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.error, this)

        elevation = 10.dpToPx().toFloat()
        isFocusable = true
        isClickable = true

        hide()
    }

    fun setError(
        title: String,
        message: String,
        retryAction: () -> Unit = { }
    ): Unit {

        error_title.text = title
        error_message.text = message

        retry.setOnClickListener {
            retryAction()
            hide()
        }

        this.visibility = View.VISIBLE
    }

    fun hide(): Unit {
        this.visibility = View.GONE
    }
}