package org.fouryouandme.auth.integration.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_login.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.IntegrationSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*
import timber.log.Timber
import java.net.URL

class IntegrationLoginFragment : IntegrationSectionFragment(R.layout.integration_login) {

    private val args: IntegrationLoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        integrationAndConfiguration { config, state ->

            setupToolbar()
            applyConfiguration(config)
            setupWebView(state.cookies)

        }

    }

    private suspend fun setupToolbar(): Unit =
        evalOnMain {

            integrationFragment()
                .toolbar
                .showCloseButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            integrationNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            progress_bar.progressTintList =
                ColorStateList.valueOf(configuration.theme.primaryColorStart.color())

        }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun setupWebView(cookies: Map<String, String>): Unit =
        evalOnMain {

            val cookieManager =
                CookieManager.getInstance()
            cookies.forEach {

                val host = "${URL(args.url).protocol}://${URL(args.url).host}/"
                val cookie = "${it.key}=${it.value}"

                cookieManager.setCookie(host, cookie)
                cookieManager.flush()
            }

            web_view
                .also { it.settings.domStorageEnabled = true }
                .also { it.settings.javaScriptEnabled = true }
                .also { it.webViewClient = getWebClient() }
                .also { it.webChromeClient = getWebChromeClient() }
                .also {
                    it.addJavascriptInterface(
                        IntegrationLoginInterface(
                            {
                                startCoroutineAsync {
                                    viewModel.handleLogin(integrationNavController(), args.nextPage)
                                }
                            },
                            {
                                startCoroutineAsync {
                                    viewModel.back(
                                        integrationNavController(),
                                        authNavController(),
                                        rootNavController()
                                    )
                                }
                            }
                        ),
                        "Android"
                    )
                }

            loadPage()
        }

    private suspend fun loadPage(): Unit = evalOnMain { web_view.loadUrl(args.url) }

    private fun getWebClient(): WebViewClient =
        object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                web_view?.loadUrl(request?.url?.toString())
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

    private fun getWebChromeClient(): WebChromeClient =
        object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                progress_bar.progress = newProgress
                progress_bar.isVisible = newProgress < 100
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()

        web_view.destroy()

    }
}