package com.foryouandme.ui.userInfo.compose

import com.foryouandme.entity.mock.Mock
import org.threeten.bp.LocalDate

sealed class EntryItem {

    data class Text(
        val id: String,
        val name: String,
        val value: String,
    ) : EntryItem() {

        companion object {

            fun mock(): Text =
                Text(
                    id = "id",
                    name = Mock.name,
                    value = Mock.body
                )

        }

    }

    data class Date(
        val id: String,
        val name: String,
        val description: String?,
        val value: LocalDate?,
        val isEditable: Boolean
    ) : EntryItem() {

        companion object {

            fun mock(): Date =
                Date(
                    id = "id",
                    name = Mock.name,
                    description = Mock.body,
                    value = LocalDate.of(2021, 6, 27),
                    isEditable = true
                )

        }

    }

    data class Picker(
        val id: String,
        val name: String,
        val description: String?,
        val value: Value?,
        val values: List<Value>,
    ) : EntryItem() {

        data class Value(val id: String, val name: String)

        companion object {

            fun mock(): Picker =
                Picker(
                    id = "id",
                    name = Mock.name,
                    description = null,
                    value = Value("1", Mock.name),
                    values =
                    listOf(
                        Value("1", Mock.name),
                        Value("2", Mock.name),
                        Value("3", Mock.name),
                    )
                )

        }

    }

}