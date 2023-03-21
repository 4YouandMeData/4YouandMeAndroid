package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.studyinfo.detail.EStudyInfoType
import com.foryouandme.ui.userInfo.UserInfoFragmentDirections

fun userInfoToStudyInfoDetail(): NavigationExecution =
    {
        it.navigate(UserInfoFragmentDirections.actionUserInfoToStudyInfoDetail(EStudyInfoType.FAQ))
    }