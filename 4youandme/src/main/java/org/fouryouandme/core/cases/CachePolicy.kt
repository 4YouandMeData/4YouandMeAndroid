package org.fouryouandme.core.cases

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.Text
import org.fouryouandme.core.entity.configuration.Theme

sealed class CachePolicy {

    object MemoryFirst : CachePolicy()
    object MemoryFirstRefresh : CachePolicy()
    object DiskFirst : CachePolicy()
    object DiskFirstRefresh : CachePolicy()
    object Network : CachePolicy()

}

object Memory {

    var configuration: Option<Configuration> = None

}