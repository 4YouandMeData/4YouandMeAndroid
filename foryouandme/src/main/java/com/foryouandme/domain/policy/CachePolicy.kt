package com.foryouandme.domain.policy

sealed class Policy {

    object LocalOnly : Policy()
    object Network : Policy()
    object LocalFirst : Policy()

}