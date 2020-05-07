package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.ImageConfiguration

class SampleImageConfiguration: ImageConfiguration {

    override fun back(): Int = R.drawable.back

    override fun logo(): Int = R.drawable.logo

    override fun logoStudy(): Int = R.drawable.logo_study

    override fun welcome(): Int = R.drawable.welcome

    override fun signUp(): Int = R.drawable.sign_up
}