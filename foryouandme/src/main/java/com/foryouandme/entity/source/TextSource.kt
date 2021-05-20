package com.foryouandme.entity.source

sealed class TextSource {

    data class AndroidRes(val resId: Int, val args: List<Any>) : TextSource()

    data class Text(val string: String) : TextSource()

}