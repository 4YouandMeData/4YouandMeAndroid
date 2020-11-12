package com.foryouandme.core.entity.optins

import arrow.core.Option
import com.foryouandme.core.cases.permission.Permission

data class OptInsPermission(
    val id: String,
    val image: Option<String>,
    val title: String,
    val body: String,
    val position: Int,
    val agreeText: String,
    val disagreeText: String,
    val systemPermissions: List<Permission>,
    val mandatory: Boolean,
    val mandatoryDescription: Option<String>
)