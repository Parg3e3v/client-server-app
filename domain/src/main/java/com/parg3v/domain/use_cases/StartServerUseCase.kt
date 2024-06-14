package com.parg3v.domain.use_cases

import com.parg3v.domain.repository.ServerRepository
import javax.inject.Inject


class StartServerUseCase @Inject constructor(private val repository: ServerRepository) {
    operator fun invoke(port: Int) {
        repository.startServer(port)
    }
}