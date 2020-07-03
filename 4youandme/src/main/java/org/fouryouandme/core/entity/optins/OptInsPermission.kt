package org.fouryouandme.core.entity.optins

data class OptInsPermission(
    val id: String,
    val title: String,
    val body: String,
    val position: Int,
    val agreeText: String,
    val disagreeText: String,
    val systemPermissions: List<String>
)