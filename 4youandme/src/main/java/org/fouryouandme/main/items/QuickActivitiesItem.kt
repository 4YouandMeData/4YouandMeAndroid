package org.fouryouandme.main.items

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.quick_activities_item.*
import org.fouryouandme.R

data class QuickActivitiesItem(val id: String, val quickActivities: DroidAdapter) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        other.compare<QuickActivitiesItem> { it.id == id }

    override fun haveTheSameContent(other: DroidItem): Boolean =

        other.compare<QuickActivitiesItem> { it ->

            val old =
                it.quickActivities.getItems().mapNotNull { it as? QuickActivityItem }
            val new =
                quickActivities.getItems().mapNotNull { it as? QuickActivityItem }

            if (old.size == new.size)
                old.foldIndexed(true) { index,
                                        acc,
                                        quickActivityItem ->
                    acc && new[index].haveTheSameContent(quickActivityItem)
                }
            else false

        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()
}


class QuickActivitiesViewHolder(
    viewGroup: ViewGroup
) : DroidViewHolder<QuickActivitiesItem, Unit>(
    viewGroup,
    R.layout.quick_activities_item
), LayoutContainer {

    override fun bind(t: QuickActivitiesItem, position: Int) {

        view_pager.adapter = t.quickActivities

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { QuickActivitiesViewHolder(it) },
                { _, item -> item is QuickActivitiesItem }
            )

    }
}