package com.foryouandme.domain.usecase.study

import com.foryouandme.entity.study.Study
import javax.inject.Inject

class GetStudyUseCase @Inject constructor(
    private val repository: StudyRepository
) {

    suspend operator fun invoke(): Study =
        repository.fetchStudy()

}