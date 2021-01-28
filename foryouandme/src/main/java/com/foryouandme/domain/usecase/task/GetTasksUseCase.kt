package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.task.Task
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(order: Order, page: Int, pageSize: Int): PagedList<Task> =

        repository.getTasks(
            getTokenUseCase(),
            order,
            page,
            pageSize
        )

}