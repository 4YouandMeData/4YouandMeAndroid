package org.fouryouandme.auth.consent.question

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.StableDroidItem
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_answer.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.question.EConsentAnswerPayload.ANSWER
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.ConsentAnswer

enum class EConsentAnswerPayload {

    ANSWER

}

data class ConsentAnswerItem(
    val answer: ConsentAnswer,
    val isSelected: Boolean,
    val configuration: Configuration
) : StableDroidItem {

    override fun stableId(position: Int): Long = position.hashCode().toLong()

    override fun areTheSame(other: DroidItem): Boolean =
        if (other is ConsentAnswerItem) answer.id == other.answer.id else false

    override fun haveTheSameContent(other: DroidItem): Boolean =
        if (other is ConsentAnswerItem)
            answer.id == other.answer.id &&
                    answer.text == other.answer.text &&
                    isSelected == other.isSelected
        else false

    override fun getPayload(other: DroidItem): List<*> {

        val payload = mutableListOf<EConsentAnswerPayload>()

        if (other is ConsentAnswerItem)
            if (isSelected != other.isSelected)
                payload.add(ANSWER)

        return payload
    }
}

fun ConsentAnswer.toItem(configuration: Configuration): ConsentAnswerItem =
    ConsentAnswerItem(this, false, configuration)

class ConsentAnswerViewHolder(
    parent: ViewGroup,
    onAnswer: (ConsentAnswerItem) -> Unit
) : DroidViewHolder<ConsentAnswerItem, EConsentAnswerPayload>(
    parent,
    { layoutInflater: LayoutInflater, viewGroup: ViewGroup, b: Boolean ->
        layoutInflater.inflate(
            R.layout.consent_answer,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    override fun bind(t: ConsentAnswerItem, position: Int) {

        answer.text = t.answer.text
        answer.setTextColor(t.configuration.theme.secondaryColor.color())

        radio_button.isChecked = t.isSelected
        radio_button.buttonTintList =
            ColorStateList.valueOf(t.configuration.theme.secondaryColor.color())

    }

    override fun bind(t: ConsentAnswerItem, position: Int, payloads: List<EConsentAnswerPayload>) {

        payloads.forEach {
            when (it) {
                ANSWER -> radio_button.isChecked = t.isSelected
            }
        }
    }

    override val containerView: View? = itemView

    companion object {

        fun factory(onAnswer: (ConsentAnswerItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { ConsentAnswerViewHolder(it, onAnswer) as DroidViewHolder<DroidItem, *> },
                { _, droidItem -> droidItem is ConsentAnswerItem }
            )
    }
}