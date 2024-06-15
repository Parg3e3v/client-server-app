package com.parg3v.client_serverapp.model

import com.parg3v.domain.common.RootError
import com.parg3v.domain.model.GestureLog

data class LogsStatus(
    val isLoading: Boolean = false,
    val data: List<GestureLog>? = null,
    val error: RootError? = null
)