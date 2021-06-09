package com.foryouandme.core.arch.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.databinding.ErrorBinding

class ErrorView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    val binding: ErrorBinding =
        ErrorBinding.inflate(LayoutInflater.from(context), this, true)

    init {

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

        binding.errorTitle.text = title
        binding.errorMessage.text = message

        binding.retry.setOnClickListener {
            retryAction()
            hide()
        }

        this.visibility = View.VISIBLE
    }

    fun hide(): Unit {
        this.visibility = View.GONE
    }
}