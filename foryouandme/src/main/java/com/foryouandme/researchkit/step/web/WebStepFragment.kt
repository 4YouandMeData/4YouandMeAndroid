package com.foryouandme.researchkit.step.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.databinding.StepWebBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.net.URL

@AndroidEntryPoint
class WebStepFragment : StepFragment(R.layout.step_web) {

    private val binding: StepWebBinding?
        get() = view?.let { StepWebBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStepByIndexAs<WebStep>(indexArg())?.let { applyData(it) }

    }

    private fun applyData(step: WebStep) {

        val viewBinding = binding

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.progressBar?.progressTintList = ColorStateList.valueOf(step.progressBarColor)

        step.javascriptInterface?.setListener(

            object : JavaScriptInterfaceListener {

                override fun nextStep() {
                    next()
                }

                override fun close() {
                    this@WebStepFragment.close()
                }

            }

        )

        viewBinding?.webView?.setupWebView(
            step.url,
            step.cookies,
            step.javascriptInterface,
            step.javascriptInterfaceName
        )

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun WebView.setupWebView(
        url: String,
        cookies: Map<String, String>,
        javascriptInterface: Any?,
        javascriptInterfaceName: String?
    ) {

        val viewBinding = binding

        if (viewBinding != null) {

            val cookieManager = CookieManager.getInstance()

            cookies.forEach {

                val host = "${URL(url).protocol}://${URL(url).host}/"
                val cookie = "${it.key}=${it.value}"

                cookieManager.setCookie(host, cookie)
                cookieManager.flush()
            }

            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            webViewClient = getWebClient()
            webChromeClient = getWebChromeClient(viewBinding.progressBar)

            mapNotNull(javascriptInterface, javascriptInterfaceName)
                ?.let { addJavascriptInterface(it.a, it.b) }

            loadUrl(url)

        }
    }

    private fun getWebClient(): WebViewClient =
        object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url?.toString())
                return true
            }

            @SuppressLint("BinaryOperationInTimber")
            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Timber.e(
                    "${request?.url.toString()}: error:" +
                            " [${error.errorCode}] ${error.description}"
                )
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Timber.e("${failingUrl ?: ""}: error: $errorCode $description")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

        }

    private fun getWebChromeClient(progressBar: ProgressBar): WebChromeClient =
        object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                progressBar.progress = newProgress
                progressBar.isVisible = newProgress < 100

            }
        }

    override fun onDestroyView() {

        binding?.webView?.destroy()

        super.onDestroyView()

    }

}