package com.parg3v.domain.use_cases.client

import com.parg3v.domain.repository.ClientRepository
import javax.inject.Inject

class StopClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(){
        clientRepository.stopClient()
    }
}