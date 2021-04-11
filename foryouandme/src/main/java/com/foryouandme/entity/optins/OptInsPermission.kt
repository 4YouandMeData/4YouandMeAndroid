package com.foryouandme.entity.optins

import com.foryouandme.entity.permission.Permission

data class OptInsPermission(
    val id: String,
    val image: String?,
    val title: String,
    val body: String,
    val position: Int,
    val agreeText: String,
    val disagreeText: String,
    val systemPermissions: List<Permission>,
    val mandatory: Boolean,
    val mandatoryDescription: String?
)