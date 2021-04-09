package com.foryouandme.ui.aboutyou.dailysurveytime

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.*
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import kotlinx.android.synthetic.main.daily_survey_time.*

class AboutYouDailySurveyTimeFragment :
    AboutYouSectionFragment<AboutYouDailySurveyTimeViewModel>(R.layout.daily_survey_time) {
    override val viewModel: AboutYouDailySurveyTimeViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                AboutYouDailySurveyTimeViewModel(
                    navigator,
                    injector.dailySurveyTimeModule(),
                    injector.analyticsModule()
                )
            }
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            applyConfiguration(it)
        }

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logScreenViewed() }

    }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())
            toolbar.showBackButtonSuspend(imageConfiguration) {
                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
            }

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.profile.fifthItem

            description.setTextColor(configuration.theme.primaryTextColor.color())
            description.text = configuration.text.profile.dailySurveyTimeDescription

            time_picker.setIs24HourView(false)

            save_button.text = "Save"
            save_button.setTextColor(configuration.theme.secondaryTextColor.color())
            save_button.background = button(configuration.theme.primaryColorStart.color())
            save_button.setOnClickListener { }
        }


}