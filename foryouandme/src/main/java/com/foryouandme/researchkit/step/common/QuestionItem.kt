package com.foryouandme.researchkit.step.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.researchkit.utils.ImageResource
import com.foryouandme.researchkit.utils.applyImage
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.step_question_item.*

data class QuestionItem(
    val id: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val image: ImageResource?
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<QuestionItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<QuestionItem> { it == this }

}

class QuestionViewHolder(
    parent: ViewGroup
) : DroidViewHolder<QuestionItem, Unit>(parent, R.layout.step_question_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: QuestionItem, position: Int) {

        t.image?.let { icon.applyImage(it) }
        icon.isVisible = t.image != null

        question.text = t.question(context)
        question.setTextColor(t.questionColor)

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { QuestionViewHolder(it) },
                { _, item -> item is QuestionItem }
            )

    }

}