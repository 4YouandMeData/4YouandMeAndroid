package org.fouryouandme.htmldetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.html_detail.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class HtmlDetailsFragment : BaseFragment<HtmlDetailsViewModel>(R.layout.html_detail) {

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

            detailsToolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

            backArrow.setImageResource(imageConfiguration.back())
            backArrow.setOnClickListener {
                startCoroutineAsync { viewModel.back(findNavController()) }
            }

            title.setTextColor(configuration.theme.secondaryColor.color())

        }

    private suspend fun setupWebView(pageId: Int): Unit =
        evalOnMain {

            var source = ""
            when (pageId) {
                0 -> {
                    title.text = viewModel.state().studyInfo.informationPage.title
                    source = viewModel.state().studyInfo.informationPage.body
                }
                1 -> {
                    title.text = viewModel.state().studyInfo.rewardPage.title
                    source = viewModel.state().studyInfo.rewardPage.body
                }
                2 -> {
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

            web_view.loadDataWithBaseURL(null, source, "text/html; charset=utf-8", "utf-8", null)
        }
}