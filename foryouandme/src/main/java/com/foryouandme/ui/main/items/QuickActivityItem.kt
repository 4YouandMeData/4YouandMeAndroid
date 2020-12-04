package com.foryouandme.ui.main.items

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.entity.activity.QuickActivity
import com.foryouandme.core.entity.activity.QuickActivityAnswer
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.ext.getOr
import com.foryouandme.ui.main.items.EQuickActivityPayload.SELECTED_ANSWER
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.providePayload
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.quick_activity_item.*

enum class EQuickActivityPayload {
    SELECTED_ANSWER
}

data class QuickActivityItem(
    val configuration: Configuration,
    val data: QuickActivity,
    val selectedAnswer: String?
) : DroidItem<EQuickActivityPayload> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<QuickActivityItem> { it.data.id == data.id }

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<QuickActivityItem> {
            it.selectedAnswer == selectedAnswer &&
                    it.data.id == data.id &&
                    it.data.answer1 == data.answer1 &&
                    it.data.answer2 == data.answer2 &&
                    it.data.answer3 == data.answer3 &&
                    it.data.answer4 == data.answer4 &&
                    it.data.answer5 == data.answer5 &&
                    it.data.answer6 == data.answer6
        }

    override fun getPayload(other: DroidItem<Any>): List<EQuickActivityPayload> =
        other.providePayload<EQuickActivityPayload, QuickActivityItem> {
            when {
                it.selectedAnswer != selectedAnswer ->
                    listOf(SELECTED_ANSWER)
                else ->
                    emptyList()
            }
        }
}


class QuickActivityViewHolder(
    viewGroup: ViewGroup,
    val onAnswerSelected: (QuickActivityItem, QuickActivityAnswer) -> Unit,
    val onSubmitClicked: (QuickActivityItem) -> Unit
) : DroidViewHolder<QuickActivityItem, EQuickActivityPayload>(
    viewGroup,
    R.layout.quick_activity_item
), LayoutContainer {

    init {

        R.id.review.getView<View>().setOnClickListener { onSubmitClicked(item) }

    }

    override fun bind(item: QuickActivityItem, position: Int) {

        card_content.background =
            HEXGradient.from(
                item.configuration.theme.primaryColorStart,
                item.configuration.theme.primaryColorEnd
            ).drawable()

        title.text = item.data.title.orEmpty()
        title.setTextColor(item.configuration.theme.secondaryColor.color())
        title.alpha = 0.5f

        body.text = item.data.description.orEmpty()
        body.setTextColor(item.configuration.theme.secondaryColor.color())

        getAnswerImage(item.selectedAnswer, item.data.answer1)?.let { answer_1.setImageBitmap(it) }
        getAnswerImage(item.selectedAnswer, item.data.answer2)?.let { answer_2.setImageBitmap(it) }
        getAnswerImage(item.selectedAnswer, item.data.answer3)?.let { answer_3.setImageBitmap(it) }
        getAnswerImage(item.selectedAnswer, item.data.answer4)?.let { answer_4.setImageBitmap(it) }
        getAnswerImage(item.selectedAnswer, item.data.answer5)?.let { answer_5.setImageBitmap(it) }
        getAnswerImage(item.selectedAnswer, item.data.answer6)?.let { answer_6.setImageBitmap(it) }

        answer_1.isEnabled = item.data.answer1 != null
        answer_2.isEnabled = item.data.answer2 != null
        answer_2.isEnabled = item.data.answer3 != null
        answer_4.isEnabled = item.data.answer4 != null
        answer_5.isEnabled = item.data.answer5 != null
        answer_6.isEnabled = item.data.answer6 != null

        answer_1_text.text = item.data.answer1?.text.orEmpty()
        answer_2_text.text = item.data.answer2?.text.orEmpty()
        answer_3_text.text = item.data.answer3?.text.orEmpty()
        answer_4_text.text = item.data.answer4?.text.orEmpty()
        answer_5_text.text = item.data.answer5?.text.orEmpty()
        answer_6_text.text = item.data.answer6?.text.orEmpty()

        answer_1_text.setTextColor(item.configuration.theme.secondaryColor.color())
        answer_2_text.setTextColor(item.configuration.theme.secondaryColor.color())
        answer_3_text.setTextColor(item.configuration.theme.secondaryColor.color())
        answer_4_text.setTextColor(item.configuration.theme.secondaryColor.color())
        answer_5_text.setTextColor(item.configuration.theme.secondaryColor.color())
        answer_6_text.setTextColor(item.configuration.theme.secondaryColor.color())

        answer_1.setOnClickListener { item.data.answer1?.let { onAnswerSelected(item, it) } }
        answer_2.setOnClickListener { item.data.answer2?.let { onAnswerSelected(item, it) } }
        answer_3.setOnClickListener { item.data.answer3?.let { onAnswerSelected(item, it) } }
        answer_4.setOnClickListener { item.data.answer4?.let { onAnswerSelected(item, it) } }
        answer_5.setOnClickListener { item.data.answer5?.let { onAnswerSelected(item, it) } }
        answer_6.setOnClickListener { item.data.answer6?.let { onAnswerSelected(item, it) } }

        review.isEnabled = item.selectedAnswer != null
        review.background = button(item.configuration.theme.secondaryColor.color())
        review.setTextColor(item.configuration.theme.primaryTextColor.color())
        review.text =
            item.data.button.getOr { item.configuration.text.activity.quickActivityButtonDefault }

    }

    override fun bind(item: QuickActivityItem, position: Int, payloads: List<EQuickActivityPayload>) {
        super.bind(item, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                SELECTED_ANSWER -> {

                    answer_1.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer1)
                    )
                    answer_2.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer2)
                    )
                    answer_3.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer3)
                    )
                    answer_4.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer4)
                    )
                    answer_5.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer5)
                    )
                    answer_6.setImageBitmap(
                        getAnswerImage(item.selectedAnswer, item.data.answer6)
                    )

                    review.isEnabled = item.selectedAnswer != null
                }
            }
        }
    }

    private fun getAnswerImage(
        selectedAnswer: String?,
        answer: QuickActivityAnswer?
    ): Bitmap? =
        answer?.let { quickActivityAnswer ->

            if (selectedAnswer == quickActivityAnswer.id)
                quickActivityAnswer.selectedImage
            else
                quickActivityAnswer.image

        }

    override val containerView: View? = itemView

    companion object {

        fun factory(
            onAnswerSelected: (QuickActivityItem, QuickActivityAnswer) -> Unit,
            onSubmitClicked: (QuickActivityItem) -> Unit
        ): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { QuickActivityViewHolder(it, onAnswerSelected, onSubmitClicked) },
                { _, item -> item is QuickActivityItem }
            )

    }
}