package com.foryouandme.researchkit.step.choosemany

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.ChooseManyAnswerBinding
import com.foryouandme.researchkit.utils.DebounceTextListener
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory

data class ChooseManyAnswer(
    val id: String,
    val text: String,
    val textColor: Int,
    val buttonColor: Int,
    val isNone: Boolean,
    val isOther: Boolean,
    val otherPlaceholder: String?
)

data class ChooseManyAnswerItem(
    val id: String,
    val text: String,
    val isSelected: Boolean,
    val textColor: Int,
    val buttonColor: Int,
    val isNone: Boolean,
    val otherText: String?,
    val otherPlaceholder: String?
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
    private val onAnswerClicked: (ChooseManyAnswerItem) -> Unit,
    private val onTextChanged: (ChooseManyAnswerItem, String) -> Unit
) : DroidViewHolder<ChooseManyAnswerItem, Unit>(parent, R.layout.choose_many_answer) {

    override fun bind(item: ChooseManyAnswerItem, position: Int) {

        val binding = ChooseManyAnswerBinding.bind(itemView)

        binding.answerText.text = item.text
        binding.answerText.setTextColor(item.textColor)

        binding.answerEditText.clearTextChangedListeners()
        if (binding.answerEditText.text?.toString().orEmpty() != item.otherText)
            binding.answerEditText.setText(item.otherText)
        binding.answerEditText.hint = item.otherPlaceholder.orEmpty()
        binding.answerEditText.setTextColor(item.textColor)
        binding.answerEditText.isVisible = item.otherText != null && item.isSelected
        if (item.otherText != null) {
            binding.answerEditText.addTextChangedListener(
                DebounceTextListener { onTextChanged(item, it) }
            )
        }

        binding.answerButton.isChecked = item.isSelected
        binding.answerButton.buttonTintList =
            ColorStateList.valueOf(item.buttonColor)

        binding.answerButton.setOnClickListener {
            onAnswerClicked(item)
        }

        binding.answerText.setOnClickListener {
            onAnswerClicked(item)
        }
    }

    companion object {

        fun factory(
            onAnswerClicked: (ChooseManyAnswerItem) -> Unit,
            onTextChanged: (ChooseManyAnswerItem, String) -> Unit
        ): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ChooseManyAnswerViewHolder(it, onAnswerClicked, onTextChanged) },
                { _, droidItem -> droidItem is ChooseManyAnswerItem }
            )
    }

}