package com.parg3v.domain.use_cases.server

import com.parg3v.domain.repository.GestureLogRepository
import javax.inject.Inject

class ClearLogsFormDBUseCase @Inject constructor(private val repository: GestureLogRepository) {
    suspend operator fun invoke(){
        repository.clearAllLogs()
    }
}