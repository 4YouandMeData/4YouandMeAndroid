package org.fouryouandme.core.cases

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

    var configuration: Configuration? = null

    var token: String? = null

}