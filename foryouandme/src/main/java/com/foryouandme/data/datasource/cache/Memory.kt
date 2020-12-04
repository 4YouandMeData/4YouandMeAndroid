package com.foryouandme.data.datasource.cache

import com.foryouandme.entity.configuration.Configuration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Memory @Inject constructor() {

    var configuration: Configuration? = null

}