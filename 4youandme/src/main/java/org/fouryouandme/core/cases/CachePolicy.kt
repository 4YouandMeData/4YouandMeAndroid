package org.fouryouandme.core.cases

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

sealed class CachePolicy {

    object MemoryFirst : CachePolicy()
    object MemoryFirstRefresh : CachePolicy()
    object MemoryOrDisk : CachePolicy()
    object DiskFirst : CachePolicy()
    object DiskFirstRefresh : CachePolicy()
    object Network : CachePolicy()

}

object Memory {

    var configuration: Option<Configuration> = None

}