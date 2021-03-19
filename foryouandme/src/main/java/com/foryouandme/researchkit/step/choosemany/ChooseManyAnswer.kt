package com.foryouandme.researchkit.step.choosemany

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.choose_many_answer.*

data class ChooseManyAnswer(
    val id: String,
    val text: String,
    val textColor: Int,
    val buttonColor: Int,
    val isNone: Boolean
)

data class ChooseManyAnswerItem(
    val id: String,
    val text: String,
    val isSelected: Boolean,
    val textColor: Int,
    val buttonColor: Int,
    val isNone: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<ChooseManyAnswerItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<ChooseManyAnswerItem> {
            it == this
        }
}

class ChooseManyAnswerViewHolder(
    parent: ViewGroup,
    private val onAnswerClicked: (ChooseManyAnswerItem) -> Unit
) :
    DroidViewHolder<ChooseManyAnswerItem, Unit>(parent, R.layout.choose_many_answer),
    LayoutContainer {

    override fun bind(item: ChooseManyAnswerItem, position: Int) {

        answer_text.text = item.text
        answer_text.setTextColor(item.textColor)

        answer_button.isChecked = item.isSelected
        answer_button.buttonTintList =
            ColorStateList.valueOf(item.buttonColor)

        answer_button.setOnClickListener {
            onAnswerClicked(item)
        }

        answer_text.setOnClickListener {
            onAnswerClicked(item)
        }
    }


    override val containerView: View? = itemView

    companion object {

        fun factory(onAnswerClicked: (ChooseManyAnswerItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ChooseManyAnswerViewHolder(it, onAnswerClicked) },
                { _, droidItem -> droidItem is ChooseManyAnswerItem }
            )
    }
}