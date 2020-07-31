package org.fouryouandme.main.items

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import arrow.core.Option
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
    viewGroup: ViewGroup
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

        title.text = t.data.title
        title.setTextColor(t.configuration.theme.secondaryColor.color())
        title.alpha = 0.5f

        body.text = t.data.description
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        getAnswerImage(t.selectedAnswer, t.data.answer1).map { answer_1.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer2).map { answer_2.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer3).map { answer_3.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer4).map { answer_4.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer5).map { answer_5.setImageBitmap(it) }
        getAnswerImage(t.selectedAnswer, t.data.answer6).map { answer_6.setImageBitmap(it) }

        answer_1_text.text = t.data.answer1.text
        answer_2_text.text = t.data.answer2.text
        answer_3_text.text = t.data.answer3.text
        answer_4_text.text = t.data.answer4.text
        answer_5_text.text = t.data.answer5.text
        answer_6_text.text = t.data.answer6.text

        button.background = button(t.configuration.theme.secondaryColor.color())
        button.text = t.data.button
        button.setTextColor(t.configuration.theme.primaryTextColor.color())

    }

    override fun bind(t: QuickActivityItem, position: Int, payloads: List<EQuickActivityPayload>) {
        super.bind(t, position, payloads)

        payloads.forEach { payload ->
            when (payload) {
                SELECTED_ANSWER -> {
                    getAnswerImage(t.selectedAnswer, t.data.answer1)
                        .map { answer_1.setImageBitmap(it) }
                    getAnswerImage(t.selectedAnswer, t.data.answer2)
                        .map { answer_2.setImageBitmap(it) }
                    getAnswerImage(t.selectedAnswer, t.data.answer3)
                        .map { answer_3.setImageBitmap(it) }
                    getAnswerImage(t.selectedAnswer, t.data.answer4)
                        .map { answer_4.setImageBitmap(it) }
                    getAnswerImage(t.selectedAnswer, t.data.answer5)
                        .map { answer_5.setImageBitmap(it) }
                    getAnswerImage(t.selectedAnswer, t.data.answer6)
                        .map { answer_6.setImageBitmap(it) }
                }
            }
        }
    }

    private fun getAnswerImage(
        selectedAnswer: Option<String>,
        answer: QuickActivityAnswer
    ): Option<Bitmap> =
        if (selectedAnswer == answer.id.toOption())
            answer.selectedImage.decodeBase64Image()
        else
            answer.image.decodeBase64Image()


    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { QuickActivityViewHolder(it) },
                { _, item -> item is QuickActivityItem }
            )

    }
}