package com.foryouandme.core.ext.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.webkit.*
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.entity.user.User
import timber.log.Timber
import java.net.URL


@SuppressLint("SetJavaScriptEnabled")
suspend fun WebView.setupWebViewWithCookiesSuspend(
    progressBar: ProgressBar,
    url: String,
    cookies: Map<String, String>,
    success: () -> Unit,
    failure: () -> Unit
) {

    evalOnMain {
        setupWebViewWithCookiesSuspend(progressBar, url, cookies, success, failure)
    }

}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setupWebViewWithCookies(
    progressBar: ProgressBar,
    url: String,
    cookies: Map<String, String>,
    success: () -> Unit,
    failure: () -> Unit
) {

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
    webChromeClient = getWebChromeClient(progressBar)

    addJavascriptInterface(
        IntegrationLoginInterface(
            { success() },
            { failure() }
        ),
        "Android"
    )

    loadUrl(url)

}

private fun getWebClient(): WebViewClient =
    object : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url?.toString().orEmpty())
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

fun String.asIntegrationCookies(): Map<String, String> = mapOf("token" to this)

fun User.getIntegrationCookies(): Map<String, String> = token.asIntegrationCookies()


