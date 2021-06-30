package com.foryouandme.entity.source

sealed class ColorSource {

    data class AndroidRes(val resId: Int) : ColorSource()

}