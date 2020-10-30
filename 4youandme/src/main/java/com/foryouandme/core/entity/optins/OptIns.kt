package com.foryouandme.core.entity.optins

import com.foryouandme.core.entity.page.Page

data class OptIns(
    val welcomePage: Page,
    val successPage: Page,
    val permissions: List<OptInsPermission>
)