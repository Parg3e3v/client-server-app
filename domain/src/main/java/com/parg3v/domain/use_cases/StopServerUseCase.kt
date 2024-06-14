package com.parg3v.domain.use_cases

import com.parg3v.domain.repository.ServerRepository
import javax.inject.Inject


class StopServerUseCase @Inject constructor(private val repository: ServerRepository) {
    operator fun invoke() {
        repository.stopServer()
    }
}