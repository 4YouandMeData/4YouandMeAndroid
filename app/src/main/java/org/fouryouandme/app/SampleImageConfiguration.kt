package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.ImageConfiguration

class SampleImageConfiguration: ImageConfiguration {

    /* --- common ---*/

    override fun back(): Int = R.drawable.back

    override fun logo(): Int = R.drawable.logo

    override fun logoStudy(): Int = R.drawable.logo_study

    /* --- auth --- */

    override fun welcome(): Int = R.drawable.welcome

    override fun signUp(): Int = R.drawable.sign_up

    override fun phoneWrong(): Int = R.drawable.phone_wrong

    override fun phoneValid(): Int = R.drawable.phone_valid
}