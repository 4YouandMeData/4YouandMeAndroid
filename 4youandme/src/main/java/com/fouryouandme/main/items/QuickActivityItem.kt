package com.fouryouandme.main.items

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import com.fouryouandme.R
import com.fouryouandme.core.entity.activity.QuickActivity
import com.fouryouandme.core.entity.activity.QuickActivityAnswer
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.configuration.HEXGradient
import com.fouryouandme.core.entity.configuration.button.button
import com.fouryouandme.core.ext.getOr
import com.fouryouandme.main.items.EQuickActivityPayload.SELECTED_ANSWER
import com.giacomoparisi.recyclerdroid.core.*
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

    override fun bind(t: QuickActivityItem, position: Int) {

        card_content.background =
            HEXGradient.from(
                t.configuration.theme.primaryColorStart,
                t.configuration.theme.primaryColorEnd
            ).drawable()

        title.text = t.data.title.orEmpty()
        title.setTextColor(t.configuration.theme.secondaryColor.color())
        title.alpha = 0.5f

        body.text = t.data.description.orEmpty()
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        getAnswerImage(t.selectedAnswer, t.data.answer1)?.let { answer_1.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer2)?.let { answer_2.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer3)?.let { answer_3.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer4)?.let { answer_4.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer5)?.let { answer_5.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer6)?.let { answer_6.setImageBitmap(it) }

        answer_1.isEnabled = t.data.answer1 != null
        answer_2.isEnabled = t.data.answer2 != null
        answer_2.isEnabled = t.data.answer3 != null
        answer_4.isEnabled = t.data.answer4 != null
        answer_5.isEnabled = t.data.answer5 != null
        answer_6.isEnabled = t.data.answer6 != null

        answer_1_text.text = t.data.answer1?.text.orEmpty()
        answer_2_text.text = t.data.answer2?.text.orEmpty()
        answer_3_text.text = t.data.answer3?.text.orEmpty()
        answer_4_text.text = t.data.answer4?.text.orEmpty()
        answer_5_text.text = t.data.answer5?.text.orEmpty()
        answer_6_text.text = t.data.answer6?.text.orEmpty()

        answer_1_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_2_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_3_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_4_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_5_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_6_text.setTextColor(t.configuration.theme.secondaryColor.color())

        answer_1.setOnClickListener { t.data.answer1?.let { onAnswerSelected(t, it) } }
        answer_2.setOnClickListener { t.data.answer2?.let { onAnswerSelected(t, it) } }
        answer_3.setOnClickListener { t.data.answer3?.let { onAnswerSelected(t, it) } }
        answer_4.setOnClickListener { t.data.answer4?.let { onAnswerSelected(t, it) } }
        answer_5.setOnClickListener { t.data.answer5?.let { onAnswerSelected(t, it) } }
        answer_6.setOnClickListener { t.data.answer6?.let { onAnswerSelected(t, it) } }

        review.isEnabled = t.selectedAnswer != null
        review.background = button(t.configuration.theme.secondaryColor.color())
        review.setTextColor(t.configuration.theme.primaryTextColor.color())
        review.text =
            t.data.button.getOr { t.configuration.text.activity.quickActivityButtonDefault }

    }

    override fun bind(t: QuickActivityItem, position: Int, payloads: List<EQuickActivityPayload>) {
        super.bind(t, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                SELECTED_ANSWER -> {

                    answer_1.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer1)
                    )
                    answer_2.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer2)
                    )
                    answer_3.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer3)
                    )
                    answer_4.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer4)
                    )
                    answer_5.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer5)
                    )
                    answer_6.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer6)
                    )

                    review.isEnabled = t.selectedAnswer != null
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
        ): ViewHolderFactory =
            ViewHolderFactory(
                { QuickActivityViewHolder(it, onAnswerSelected, onSubmitClicked) },
                { _, item -> item is QuickActivityItem }
            )

    }
}