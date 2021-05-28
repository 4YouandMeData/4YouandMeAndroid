package com.foryouandme.ui.compose.lazydata

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.error.Error
import com.foryouandme.ui.compose.loading.Loading

@Composable
fun <T> LoadingError(
    data: LazyData<T>,
    configuration: Configuration,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit,
    composable: @Composable (T) -> Unit
) {
    Box(modifier = modifier, ) {
        when (data) {
            is LazyData.Data ->
                composable(data.value)
            is LazyData.Error ->
                Error(
                    error = data.error,
                    configuration = configuration,
                    modifier = Modifier.fillMaxSize(),
                    retry = onRetryClicked
                )
            is LazyData.Loading -> {
                if (data.value != null)
                    composable(data.value)
                Loading(
                    configuration = configuration,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {

            }
        }
    }
}