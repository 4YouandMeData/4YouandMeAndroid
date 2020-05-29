package org.fouryouandme.auth.screening.questions

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.screening_question.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.questions.EScreeningQuestionPayload.ANSWER
import org.fouryouandme.auth.screening.questions.EScreeningQuestionPayload.NONE
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.ScreeningQuestion

interface DroidItem {

    fun areTheSame(other: DroidItem): Boolean

    fun haveTheSameContent(other: DroidItem): Boolean

    fun getPayload(other: DroidItem): List<*>
}

fun screeningAdapter(
    onAnswer: (ScreeningQuestionItem) -> Unit
): StableDroidAdapter<DroidItem> =
    StableDroidAdapter<DroidItem>(
        { _, i -> i.hashCode().toLong() },
        { ScreeningQuestionViewHolder(it, onAnswer) },
        { droidItem, droidItem2 -> droidItem.areTheSame(droidItem2) },
        { droidItem, droidItem2 -> droidItem.haveTheSameContent(droidItem2) },
        { droidItem, droidItem2 -> droidItem.getPayload(droidItem2) }
    )


enum class EScreeningQuestionPayload {

    ANSWER,
    NONE

}

data class ScreeningQuestionItem(
    val configuration: Configuration,
    val question: ScreeningQuestion,
    val answer: Option<String> = None
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        if (other is ScreeningQuestionItem) question.id == other.question.id else false

    override fun haveTheSameContent(other: DroidItem): Boolean =
        if (other is ScreeningQuestionItem)
            question.id == other.question.id &&
                    question.text == other.question.text &&
                    question.answers1.id == other.question.answers1.id &&
                    question.answers1.text == other.question.answers1.text &&
                    question.answers2.id == other.question.answers2.id &&
                    question.answers2.text == other.question.answers2.text &&
                    answer.getOrElse { "" } == other.answer.getOrElse { "" }
        else false


    override fun getPayload(other: DroidItem): List<*> {

        val payload = mutableListOf<EScreeningQuestionPayload>()

        if (other is ScreeningQuestionItem)
            if (answer.getOrElse { "" } != other.answer.getOrElse { "" })
                payload.add(ANSWER)

        return payload
    }
}

fun ScreeningQuestion.toItem(configuration: Configuration): ScreeningQuestionItem =
    ScreeningQuestionItem(configuration, this)

class ScreeningQuestionViewHolder(
    parent: ViewGroup,
    onAnswer: (ScreeningQuestionItem) -> Unit
) : DroidViewHolder<ScreeningQuestionItem>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.screening_question,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    init {

        itemView.findViewById<RadioButton>(R.id.answer_1_button)
            .setOnClickListener {
                onAnswer(item.copy(answer = item.question.answers1.id.toOption()))
            }

        itemView.findViewById<RadioButton>(R.id.answer_2_button)
            .setOnClickListener {
                onAnswer(item.copy(answer = item.question.answers2.id.toOption()))
            }
    }

    override fun bind(t: ScreeningQuestionItem, position: Int) {

        question.text = t.question.text
        question.setTextColor(t.configuration.theme.primaryTextColor.color())

        answer_1_text.text = t.question.answers1.text
        answer_1_text.setTextColor(t.configuration.theme.primaryTextColor.color())

        answer_2_text.text = t.question.answers2.text
        answer_2_text.setTextColor(t.configuration.theme.primaryTextColor.color())

        answer_1_button.isChecked = t.question.answers1.id == t.answer.getOrElse { "" }
        answer_2_button.isChecked = t.question.answers2.id == t.answer.getOrElse { "" }

    }

    override fun bind(t: ScreeningQuestionItem, position: Int, payloads: MutableList<Any>) {
        super.bind(t, position, payloads)

        payloads.forEach { payloadObject ->
            (payloadObject as List<EScreeningQuestionPayload>).forEach {
                when (it) {
                    ANSWER -> {
                        answer_1_button.isChecked =
                            t.question.answers1.id == t.answer.getOrElse { "" }
                        answer_2_button.isChecked =
                            t.question.answers2.id == t.answer.getOrElse { "" }
                    }
                    NONE -> bind(t, position)
                }
            }
        }
    }

    override val containerView: View? = itemView
}