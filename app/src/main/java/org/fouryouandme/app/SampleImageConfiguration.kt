package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.ImageConfiguration

class SampleImageConfiguration: ImageConfiguration {

    override fun logo(): Int = R.drawable.logo

    override fun welcome(): Int = R.drawable.welcome

}