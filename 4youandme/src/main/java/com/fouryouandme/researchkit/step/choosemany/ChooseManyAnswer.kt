package com.fouryouandme.researchkit.step.choosemany

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import com.fouryouandme.R
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.choose_many_answer.*

data class ChooseManyAnswer(
    val id: String,
    val text: String,
    val textColor: Int,
    val buttonColor: Int
)

data class ChooseManyAnswerItem(
    val id: String,
    val text: String,
    val isSelected: Boolean,
    val textColor: Int,
    val buttonColor: Int
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

class ChooseManyAnswerViewHolder(parent: ViewGroup, private val onAnswerClicked: (ChooseManyAnswerItem) -> Unit) :
    DroidViewHolder<ChooseManyAnswerItem, Unit>(parent, R.layout.choose_many_answer),
    LayoutContainer {

    override fun bind(t: ChooseManyAnswerItem, position: Int) {

        answer_text.text = t.text
        answer_text.setTextColor(t.textColor)

        answer_button.isChecked = t.isSelected
        answer_button.buttonTintList =
            ColorStateList.valueOf(t.buttonColor)

        answer_button.setOnClickListener {
            onAnswerClicked(t)
        }

        answer_text.setOnClickListener {
            onAnswerClicked(t)
        }
    }


    override val containerView: View? = itemView

    companion object {

        fun factory(onAnswerClicked: (ChooseManyAnswerItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { ChooseManyAnswerViewHolder(it, onAnswerClicked) },
                { _, droidItem -> droidItem is ChooseManyAnswerItem }
            )
    }
}