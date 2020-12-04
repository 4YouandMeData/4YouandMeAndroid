package com.foryouandme.entity.optins

import com.foryouandme.entity.page.Page

data class OptIns(
    val welcomePage: Page,
    val successPage: Page,
    val permissions: List<OptInsPermission>
)