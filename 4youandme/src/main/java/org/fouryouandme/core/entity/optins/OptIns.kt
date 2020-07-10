package org.fouryouandme.core.entity.optins

import org.fouryouandme.core.entity.page.Page

data class OptIns(
    val welcomePage: Page,
    val successPage: Page,
    val permissions: List<OptInsPermission>
)