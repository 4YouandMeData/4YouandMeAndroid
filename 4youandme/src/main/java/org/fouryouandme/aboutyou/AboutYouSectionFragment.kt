package org.fouryouandme.aboutyou

import androidx.navigation.fragment.findNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.*

open abstract class AboutYouSectionFragment<T : BaseViewModel<*, *, *, *, *>>(contentLayoutId: Int) :
    BaseFragment<T>(contentLayoutId) {

    val aboutYouViewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            aboutYouFragment(),
            getFactory {
                AboutYouViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    private fun aboutYouFragment() : AboutYouFragment = find()

    fun aboutYouNavController(): AboutYouNavController = aboutYouFragment().aboutYouNavController()

    fun aboutYouSectionNavController(): AboutYouSectionNavController = AboutYouSectionNavController(findNavController())

}