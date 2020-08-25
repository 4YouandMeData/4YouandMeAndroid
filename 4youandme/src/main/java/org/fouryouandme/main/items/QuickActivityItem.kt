package org.fouryouandme.main.items

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.quick_activity_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.activity.QuickActivity
import org.fouryouandme.core.entity.activity.QuickActivityAnswer
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.decodeBase64Image
import org.fouryouandme.core.ext.getOrEmpty
import org.fouryouandme.main.items.EQuickActivityPayload.SELECTED_ANSWER

enum class EQuickActivityPayload {
    SELECTED_ANSWER
}

data class QuickActivityItem(
    val configuration: Configuration,
    val data: QuickActivity,
    val selectedAnswer: Option<String>
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        other.compare<QuickActivityItem> { it.data.id == data.id }

    override fun haveTheSameContent(other: DroidItem): Boolean =
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

    override fun getPayload(other: DroidItem): List<*> =
        other.providePayload<QuickActivityItem, EQuickActivityPayload> {
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
    val onAnswerSelected: (QuickActivityItem, QuickActivityAnswer) -> Unit
) : DroidViewHolder<QuickActivityItem, EQuickActivityPayload>(
    viewGroup,
    R.layout.quick_activity_item
), LayoutContainer {

    override fun bind(t: QuickActivityItem, position: Int) {

        card_content.background =
            HEXGradient.from(
                t.configuration.theme.primaryColorStart,
                t.configuration.theme.primaryColorEnd
            ).drawable()

        title.text = t.data.title.getOrEmpty()
        title.setTextColor(t.configuration.theme.secondaryColor.color())
        title.alpha = 0.5f

        body.text = t.data.description.getOrEmpty()
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        getAnswerImage(t.selectedAnswer, t.data.answer1).map { answer_1.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer2).map { answer_2.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer3).map { answer_3.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer4).map { answer_4.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer5).map { answer_5.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer6).map { answer_6.setImageBitmap(it) }

        answer_1.isEnabled = t.data.answer1.isDefined()
        answer_2.isEnabled = t.data.answer2.isDefined()
        answer_2.isEnabled = t.data.answer3.isDefined()
        answer_4.isEnabled = t.data.answer4.isDefined()
        answer_5.isEnabled = t.data.answer5.isDefined()
        answer_6.isEnabled = t.data.answer6.isDefined()

        answer_1_text.text = t.data.answer1.flatMap { it.text }.getOrEmpty()
        answer_2_text.text = t.data.answer2.flatMap { it.text }.getOrEmpty()
        answer_3_text.text = t.data.answer3.flatMap { it.text }.getOrEmpty()
        answer_4_text.text = t.data.answer4.flatMap { it.text }.getOrEmpty()
        answer_5_text.text = t.data.answer5.flatMap { it.text }.getOrEmpty()
        answer_6_text.text = t.data.answer6.flatMap { it.text }.getOrEmpty()

        answer_1_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_2_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_3_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_4_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_5_text.setTextColor(t.configuration.theme.secondaryColor.color())
        answer_6_text.setTextColor(t.configuration.theme.secondaryColor.color())

        answer_1.setOnClickListener { t.data.answer1.map { onAnswerSelected(t, it) } }
        answer_2.setOnClickListener { t.data.answer2.map { onAnswerSelected(t, it) } }
        answer_3.setOnClickListener { t.data.answer3.map { onAnswerSelected(t, it) } }
        answer_4.setOnClickListener { t.data.answer4.map { onAnswerSelected(t, it) } }
        answer_5.setOnClickListener { t.data.answer5.map { onAnswerSelected(t, it) } }
        answer_6.setOnClickListener { t.data.answer6.map { onAnswerSelected(t, it) } }

        button.isEnabled = t.selectedAnswer.isDefined()
        button.background = button(t.configuration.theme.secondaryColor.color())
        button.setTextColor(t.configuration.theme.primaryTextColor.color())
        button.text =
            t.data.button.getOrElse { t.configuration.text.activity.quickActivityButtonDefault }

    }

    override fun bind(t: QuickActivityItem, position: Int, payloads: List<EQuickActivityPayload>) {
        super.bind(t, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                SELECTED_ANSWER -> {

                    answer_1.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer1).orNull()
                    )
                    answer_2.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer2).orNull()
                    )
                    answer_3.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer3).orNull()
                    )
                    answer_4.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer4).orNull()
                    )
                    answer_5.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer5).orNull()
                    )
                    answer_6.setImageBitmap(
                        getAnswerImage(t.selectedAnswer, t.data.answer6).orNull()
                    )

                    button.isEnabled = t.selectedAnswer.isDefined()
                }
            }
        }
    }

    private fun getAnswerImage(
        selectedAnswer: Option<String>,
        answer: Option<QuickActivityAnswer>
    ): Option<Bitmap> =
        answer.flatMap { quickActivityAnswer ->
            if (selectedAnswer == quickActivityAnswer.id.toOption())
                quickActivityAnswer.selectedImage.flatMap { it.decodeBase64Image() }
            else
                quickActivityAnswer.image.flatMap { it.decodeBase64Image() }
        }

    override val containerView: View? = itemView

    companion object {

        fun factory(
            onAnswerSelected: (QuickActivityItem, QuickActivityAnswer) -> Unit
        ): ViewHolderFactory =
            ViewHolderFactory(
                { QuickActivityViewHolder(it, onAnswerSelected) },
                { _, item -> item is QuickActivityItem }
            )

    }
}