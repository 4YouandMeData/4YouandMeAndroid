package org.fouryouandme.aboutyou.permissions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.about_you_menu.root
import kotlinx.android.synthetic.main.html_detail.backArrow
import kotlinx.android.synthetic.main.html_detail.detailsToolbar
import kotlinx.android.synthetic.main.html_detail.title
import kotlinx.android.synthetic.main.permissions.*
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouPermissionsFragment :
    AboutYouSectionFragment<AboutYouPermissionsViewModel>(R.layout.permissions) {
    override val viewModel: AboutYouPermissionsViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { AboutYouPermissionsViewModel(navigator) }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(PermissionsViewHolder.factory {

            if (it.isAllowed) {
                startCoroutineAsync {
                    //                  viewModel.navigateToWeb(it.link, aboutYouNavController())
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            setupRecyclerView()
            applyConfiguration(it)
        }
    }

    private suspend fun applyConfiguration(configuration: Configuration) =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            detailsToolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

            backArrow.setImageResource(imageConfiguration.back())
            backArrow.setOnClickListener {

                startCoroutineAsync {
                    aboutYouViewModel.back(aboutYouNavController(), rootNavController())
                }
            }

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.profile.fourthItem

            adapter.submitList(viewModel.getPermissions(configuration, imageConfiguration))
        }

    private suspend fun setupRecyclerView() =
        evalOnMain {

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            recycler_view.adapter = adapter

            recycler_view.addItemDecoration(
                LinearMarginItemDecoration(
                    {
                        if (it.index == 0) 50.dpToPx()
                        else 30.dpToPx()
                    },
                    { 20.dpToPx() },
                    { 20.dpToPx() },
                    {
                        if (it.index == it.itemCount) 30.dpToPx()
                        else 0.dpToPx()
                    }
                )
            )

        }
}