package com.foryouandme.ui.htmldetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.html_detail.*

class HtmlDetailsFragment : BaseFragmentOld<HtmlDetailsViewModel>(R.layout.html_detail) {

    private val args: HtmlDetailsFragmentArgs by navArgs()

    override val viewModel: HtmlDetailsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                HtmlDetailsViewModel(
                    navigator,
                    injector.studyInfoModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is HtmlDetailsStateUpdate.Initialization -> {
                        startCoroutineAsync {
                            setupWebView(args.pageId)
                        }
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController())

            applyConfiguration(it)

        }
    }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())
            toolbar.showBackButtonSuspend(imageConfiguration) {
                viewModel.back(findNavController())
            }

            title.setTextColor(configuration.theme.secondaryColor.color())

        }

    private suspend fun setupWebView(page: EHtmlDetails): Unit =
        evalOnMain {

            var source = ""
            when (page) {
                EHtmlDetails.INFO -> {
                    title.text = viewModel.state().studyInfo.informationPage.title
                    source = viewModel.state().studyInfo.informationPage.body
                }
                EHtmlDetails.REWARD -> {
                    title.text = viewModel.state().studyInfo.rewardPage.title
                    source = viewModel.state().studyInfo.rewardPage.body
                }
                EHtmlDetails.FAQ -> {
                    title.text = viewModel.state().studyInfo.faqPage.title
                    source = viewModel.state().studyInfo.faqPage.body
                }
                else -> {
                    web_view.settings.also {
                        it.javaScriptEnabled = true
                    }
                }
            }

            web_view.settings.also {
                it.javaScriptEnabled = true
            }

            web_view.loadDataWithBaseURL(
                null,
                source,
                "text/html; charset=utf-8",
                "utf-8",
                null
            )
        }
}