package com.parg3v.domain.use_cases.server

import com.parg3v.domain.repository.ServerRepository
import javax.inject.Inject


class StopServerUseCase @Inject constructor(private val repository: ServerRepository) {
    suspend operator fun invoke() {
        repository.stopServer()
    }
}