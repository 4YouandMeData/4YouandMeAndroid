package org.fouryouandme.yourdata

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import kotlinx.android.synthetic.main.your_data_page.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.*
import org.fouryouandme.yourdata.items.YourDataButtonsViewHolder
import org.fouryouandme.yourdata.items.YourDataHeaderViewHolder

class YourDataFragment : BaseFragment<YourDataViewModel>(R.layout.your_data_page) {

    override val viewModel: YourDataViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                YourDataViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            YourDataHeaderViewHolder.factory(),
            YourDataButtonsViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is YourDataStateUpdate.Initialization ->
                        configuration { applyConfiguration(it) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        startCoroutineAsync {
            if (viewModel.isInitialized().not())
                viewModel.initialize()

            applyConfiguration(viewModel.state().configuration)
        }
    }

    private suspend fun applyConfiguration(configuration: Configuration) {
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.fourthColor.color())

            title.text = configuration.text.tab.userDataTitle
            title.setTextColor(configuration.theme.secondaryColor.color())
            title.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            adapter.submitList(viewModel.getItems(configuration, imageConfiguration))
        }
    }

    private fun setupRecyclerView() {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        recycler_view.adapter = adapter

//        recycler_view.addItemDecoration(
//            LinearMarginItemDecoration(
//                {
//                    if (it.index == 0) 0.dpToPx()
//                    else 30.dpToPx()
//                },
//                { 20.dpToPx() },
//                { 20.dpToPx() },
//                {
//                    if (it.index == it.itemCount) 30.dpToPx()
//                    else 0.dpToPx()
//                }
//            )
//        )

    }

}