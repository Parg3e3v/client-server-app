package com.parg3v.data.mappers

import com.parg3v.data.local.GestureLogEntity
import com.parg3v.domain.model.GestureLog

fun GestureLogEntity.toGestureLog(): GestureLog = GestureLog(
    timestamp = this.timestamp,
    message = this.message
)