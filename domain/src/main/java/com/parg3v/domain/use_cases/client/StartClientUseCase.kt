package com.parg3v.domain.use_cases.client

import com.parg3v.domain.repository.ClientRepository
import javax.inject.Inject

class StartClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(ip: String, port: Int) {
        clientRepository.startClient(ip, port)
    }
}