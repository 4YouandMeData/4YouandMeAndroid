package com.foryouandme.domain.policy

sealed class Policy {

    object Network : Policy()
    object LocalFirst : Policy()

}