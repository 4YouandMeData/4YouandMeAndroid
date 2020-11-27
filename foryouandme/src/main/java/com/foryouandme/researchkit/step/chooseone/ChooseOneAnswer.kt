package com.foryouandme.researchkit.step.chooseone

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.choose_one_answer.*

data class ChooseOneAnswer(
    val id: String,
    val text: String,
    val textColor: Int,
    val buttonColor: Int
)

data class ChooseOneAnswerItem(
    val id: String,
    val text: String,
    val isSelected: Boolean,
    val textColor: Int,
    val buttonColor: Int
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<ChooseOneAnswerItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<ChooseOneAnswerItem> {
            it == this
        }
}

class ChooseOneAnswerViewHolder(
    parent: ViewGroup,
    private val onAnswerClicked: (ChooseOneAnswerItem) -> Unit
) :
    DroidViewHolder<ChooseOneAnswerItem, Unit>(parent, R.layout.choose_one_answer),
    LayoutContainer {

    override fun bind(item: ChooseOneAnswerItem, position: Int) {

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

        fun factory(onAnswerClicked: (ChooseOneAnswerItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ChooseOneAnswerViewHolder(it, onAnswerClicked) },
                { _, droidItem -> droidItem is ChooseOneAnswerItem }
            )
    }
}