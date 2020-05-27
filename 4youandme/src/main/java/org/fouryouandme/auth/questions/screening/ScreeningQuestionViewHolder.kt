package org.fouryouandme.auth.questions.screening

import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import org.fouryouandme.R

class ScreeningQuestionViewHolder(
    parent: ViewGroup
) : DroidViewHolder<Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.screening_question,
            viewGroup,
            b
        )
    }
) {

    override fun bind(t: Unit, position: Int) {

    }
}