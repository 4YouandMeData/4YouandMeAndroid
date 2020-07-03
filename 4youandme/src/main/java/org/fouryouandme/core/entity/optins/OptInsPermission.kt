package org.fouryouandme.core.entity.optins

import arrow.core.Option

data class OptInsPermission(
    val id: String,
    val image: Option<String>,
    val title: String,
    val body: String,
    val position: Int,
    val agreeText: String,
    val disagreeText: String,
    val systemPermissions: List<String>,
    val required: Boolean
)