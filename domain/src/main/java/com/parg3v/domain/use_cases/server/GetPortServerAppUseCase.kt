package com.parg3v.domain.use_cases.server

import com.parg3v.domain.common.DataStoreError
import com.parg3v.domain.common.Result
import com.parg3v.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPortServerAppUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Result<String?, DataStoreError>> = flow {
        try {
            emit(Result.Loading())
            val toField = dataStoreRepository.getPortServer()
            emit(Result.Success(toField))
        } catch (e: IOException) {
            emit(Result.Error(DataStoreError.BASIC))
        }
    }
}