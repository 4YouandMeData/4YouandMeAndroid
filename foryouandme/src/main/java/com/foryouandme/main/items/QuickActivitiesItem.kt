package com.foryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.quick_activities_item.*

data class QuickActivitiesItem(
    val id: String,
    val configuration: Configuration,
    val quickActivities: DroidAdapter
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<QuickActivitiesItem> { it.id == id }

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =

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

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()
}


class QuickActivitiesViewHolder(
    viewGroup: ViewGroup
) : DroidViewHolder<QuickActivitiesItem, Unit>(
    viewGroup,
    R.layout.quick_activities_item
), LayoutContainer {

    override fun bind(item: QuickActivitiesItem, position: Int) {

        view_pager.adapter = item.quickActivities
        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val counterText = "${position + 1}/${item.quickActivities.itemCount}"
                count.text = counterText

            }
        })

        val counterText = "${position + 1}/${item.quickActivities.itemCount}"
        count.text = counterText
        count.setTextColor(item.configuration.theme.primaryTextColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { QuickActivitiesViewHolder(it) },
                { _, item -> item is QuickActivitiesItem }
            )

    }
}