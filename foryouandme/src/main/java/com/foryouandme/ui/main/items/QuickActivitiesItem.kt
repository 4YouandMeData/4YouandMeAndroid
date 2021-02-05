package com.foryouandme.ui.main.items

import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.foryouandme.R
import com.foryouandme.databinding.QuickActivitiesItemBinding
import com.foryouandme.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.holder.typeCheckDroidViewHolderFactory

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
) {

    override fun bind(item: QuickActivitiesItem, position: Int) {

        val binding = QuickActivitiesItemBinding.bind(itemView)

        binding.viewPager.adapter = item.quickActivities
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.count.text = getCountText(item)

            }
        })

        binding.viewPager.isUserInputEnabled = false

        binding.count.text = getCountText(item)
        binding.count.setTextColor(item.configuration.theme.primaryTextColor.color())

    }

    private fun getCountText(item: QuickActivitiesItem): String {

        val groupCount = item.configuration.text.activity.quickActivitiesTotalNumber.toInt()
        val totalCount = item.quickActivities.itemCount
        val currentQuestionNumber = groupCount - ((totalCount - 1) % groupCount)
        return "${currentQuestionNumber}/${groupCount}"

    }


    companion object {

        fun factory(): DroidViewHolderFactory =
            typeCheckDroidViewHolderFactory<QuickActivitiesItem> { QuickActivitiesViewHolder(it) }

    }
}