package com.foryouandme.data.ext

import retrofit2.HttpException
import retrofit2.Response

fun <T> Response<T>.unwrapResponse(): T? =
    if (isSuccessful)
        body()
    else
        throw HttpException(this)