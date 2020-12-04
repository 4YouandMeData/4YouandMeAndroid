package com.foryouandme.ui.auth.onboarding.step.consent.informed.question

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.informed.question.EConsentAnswerPayload.ANSWER
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.informed.ConsentInfoAnswer
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_info_answer.*

enum class EConsentAnswerPayload {

    ANSWER

}

data class ConsentAnswerItem(
    val answer: ConsentInfoAnswer,
    val isSelected: Boolean,
    val configuration: Configuration
) : DroidItem<EConsentAnswerPayload> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        if (other is ConsentAnswerItem) answer.id == other.answer.id else false

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        if (other is ConsentAnswerItem)
            answer.id == other.answer.id &&
                    answer.text == other.answer.text &&
                    isSelected == other.isSelected
        else false

    override fun getPayload(other: DroidItem<Any>): List<EConsentAnswerPayload> {

        val payload = mutableListOf<EConsentAnswerPayload>()

        if (other is ConsentAnswerItem)
            if (isSelected != other.isSelected)
                payload.add(ANSWER)

        return payload
    }
}

fun ConsentInfoAnswer.toItem(configuration: Configuration): ConsentAnswerItem =
    ConsentAnswerItem(
        this,
        false,
        configuration
    )

class ConsentAnswerViewHolder(
    parent: ViewGroup,
    onAnswer: (ConsentAnswerItem) -> Unit
) : DroidViewHolder<ConsentAnswerItem, EConsentAnswerPayload>(
    parent,
    { layoutInflater: LayoutInflater, viewGroup: ViewGroup, b: Boolean ->
        layoutInflater.inflate(
            R.layout.consent_info_answer,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    init {

        itemView.findViewById<RadioButton>(R.id.answer_button)
            .setOnClickListener {
                onAnswer(item)
            }

    }

    override fun bind(item: ConsentAnswerItem, position: Int) {

        answer_text.text = item.answer.text
        answer_text.setTextColor(item.configuration.theme.secondaryColor.color())

        answer_button.isChecked = item.isSelected
        answer_button.buttonTintList =
            ColorStateList.valueOf(item.configuration.theme.secondaryColor.color())

    }

    override fun bind(item: ConsentAnswerItem, position: Int, payloads: List<EConsentAnswerPayload>) {

        payloads.forEach {
            when (it) {
                ANSWER -> answer_button.isChecked = item.isSelected
            }
        }
    }

    override val containerView: View? = itemView

    companion object {

        fun factory(onAnswer: (ConsentAnswerItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ConsentAnswerViewHolder(it, onAnswer) },
                { _, droidItem -> droidItem is ConsentAnswerItem }
            )
    }
}