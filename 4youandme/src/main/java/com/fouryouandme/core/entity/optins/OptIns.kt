package com.fouryouandme.core.entity.optins

import com.fouryouandme.core.entity.page.Page

data class OptIns(
    val welcomePage: Page,
    val successPage: Page,
    val permissions: List<OptInsPermission>
)