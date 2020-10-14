package org.fouryouandme.integrations

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.webkit.*
import android.widget.ProgressBar
import androidx.core.view.isVisible
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.evalOnMain
import timber.log.Timber
import java.net.URL


@SuppressLint("SetJavaScriptEnabled")
suspend fun setupIntegrationLoginWebView(
    webView: WebView,
    progressBar: ProgressBar,
    url: String,
    cookies: Map<String, String>,
    success: () -> Unit,
    failure: () -> Unit
): Unit =
    evalOnMain {

        val cookieManager =
            CookieManager.getInstance()
        cookies.forEach {

            val host = "${URL(url).protocol}://${URL(url).host}/"
            val cookie = "${it.key}=${it.value}"

            cookieManager.setCookie(host, cookie)
            cookieManager.flush()
        }

        webView
            .also { it.settings.domStorageEnabled = true }
            .also { it.settings.javaScriptEnabled = true }
            .also { it.webViewClient = getWebClient() }
            .also { it.webChromeClient = getWebChromeClient(progressBar) }
            .also {
                it.addJavascriptInterface(
                    IntegrationLoginInterface(
                        { success() },
                        { failure() }
                    ),
                    "Android"
                )
            }

        webView.loadUrl(url)
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

fun String.asIntegrationCookies(): Map<String, String> = mapOf("token" to this)

fun User.getIntegrationCookies(): Map<String, String> = token.asIntegrationCookies()


