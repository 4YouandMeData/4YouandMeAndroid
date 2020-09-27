package org.fouryouandme.aboutyou.userInfo

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.user_info.*
import kotlinx.android.synthetic.main.user_info.frameLayout
import kotlinx.android.synthetic.main.user_info.imageView
import kotlinx.android.synthetic.main.user_info.root
import kotlinx.android.synthetic.main.user_info.title
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouUserInfoFragment :
    AboutYouSectionFragment<AboutYouUserInfoViewModel>(R.layout.user_info) {
    override val viewModel: AboutYouUserInfoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                AboutYouUserInfoViewModel(
                    navigator,
                    IORuntime
                )
            }
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        aboutYouViewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is AboutYouStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (aboutYouViewModel.isInitialized()) {
            applyConfiguration(aboutYouViewModel.state().configuration)
        }
    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        detailsToolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        frameLayout.setBackgroundColor(configuration.theme.primaryColorStart.color())

        imageView.setImageResource(imageConfiguration.logoStudySecondary())

        backArrow.setImageResource(imageConfiguration.back())
        backArrow.setOnClickListener {

            startCoroutineAsync {
                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
            }
        }

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.profile.firstItem



        first_header.text = "Your due date"
        first_validation.imageTintList =
            ColorStateList.valueOf(configuration.theme.primaryTextColor.color())

        first_entry.setBackgroundColor(color(android.R.color.transparent))

        first_entry.setTextColor(configuration.theme.primaryTextColor.color())
        //first_line.setBackgroundColor(configuration.theme.secondaryColor.color())

        first_entry.setOnFocusChangeListener { _, hasFocus ->
            first_validation.setImageResource(
                when {
                    hasFocus -> 0
                    hasFocus.not() && first_entry.text.toString().isEmpty() ->
                        imageConfiguration.entryWrong()
                    else ->
                        imageConfiguration.entryValid()
                }
            )
        }



        second_header.text = "Your baby's gender"
        second_validation.imageTintList =
            ColorStateList.valueOf(configuration.theme.primaryTextColor.color())

        second_entry.setBackgroundColor(color(android.R.color.transparent))

        second_entry.setTextColor(configuration.theme.primaryTextColor.color())

        second_entry.setOnFocusChangeListener { _, hasFocus ->
            second_validation.setImageResource(
                when {
                    hasFocus -> 0
                    hasFocus.not() && second_entry.text.toString().isEmpty() ->
                        imageConfiguration.entryWrong()
                    else ->
                        imageConfiguration.entryValid()
                }
            )
        }



        third_header.text = "Your baby's name"
        third_validation.imageTintList =
            ColorStateList.valueOf(configuration.theme.primaryTextColor.color())

        third_entry.setBackgroundColor(color(android.R.color.transparent))

        third_entry.setTextColor(configuration.theme.primaryTextColor.color())

        third_entry.setOnFocusChangeListener { _, hasFocus ->
            third_validation.setImageResource(
                when {
                    hasFocus -> 0
                    hasFocus.not() && third_entry.text.toString().isEmpty() ->
                        imageConfiguration.entryWrong()
                    else ->
                        imageConfiguration.entryValid()
                }
            )
        }

        edit_button.setImageResource(imageConfiguration.editContainer())
        pencil.setImageResource(imageConfiguration.pencil())
        edit_text.text = configuration.text.profile.edit
        edit_text.setTextColor(configuration.theme.secondaryTextColor.color())
    }
}