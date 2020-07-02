package org.fouryouandme.core.entity.optins

data class OptIns(
    val title: String,
    val description: String,
    val permissions: OptInsPermission
)