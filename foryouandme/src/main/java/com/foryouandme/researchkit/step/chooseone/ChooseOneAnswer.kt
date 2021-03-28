package com.foryouandme.researchkit.step.chooseone

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.ChooseOneAnswerBinding
import com.foryouandme.researchkit.utils.DebounceTextListener
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory

data class ChooseOneAnswer(
    val id: String,
    val text: String,
    val textColor: Int,
    val buttonColor: Int,
    val isOther: Boolean,
    val otherPlaceholder: String?
)

data class ChooseOneAnswerItem(
    val id: String,
    val text: String,
    val isSelected: Boolean,
    val textColor: Int,
    val buttonColor: Int,
    val otherText: String?,
    val otherPlaceholder: String?
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
    private val onAnswerClicked: (ChooseOneAnswerItem) -> Unit,
    private val onTextChanged: (ChooseOneAnswerItem, String) -> Unit
) : DroidViewHolder<ChooseOneAnswerItem, Unit>(parent, R.layout.choose_one_answer) {

    override fun bind(item: ChooseOneAnswerItem, position: Int) {

        val binding = ChooseOneAnswerBinding.bind(itemView)

        binding.answerText.text = item.text
        binding.answerText.setTextColor(item.textColor)
        binding.answerText.isVisible = item.otherText == null

        binding.answerEditText.clearTextChangedListeners()
        if (binding.answerEditText.text?.toString().orEmpty() != item.otherText)
            binding.answerEditText.setText(item.otherText)
        binding.answerEditText.hint = item.otherPlaceholder.orEmpty()
        binding.answerEditText.setTextColor(item.textColor)
        binding.answerEditText.isVisible = item.otherText != null
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
            onAnswerClicked: (ChooseOneAnswerItem) -> Unit,
            onTextChanged: (ChooseOneAnswerItem, String) -> Unit
        ): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ChooseOneAnswerViewHolder(it, onAnswerClicked, onTextChanged) },
                { _, droidItem -> droidItem is ChooseOneAnswerItem }
            )
    }
}