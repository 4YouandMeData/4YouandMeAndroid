package com.foryouandme.entity.resources

sealed class TextResource {

    data class AndroidRes(val resId: Int, val args: List<Any>) : TextResource()

    data class Text(val string: String) : TextResource()

}