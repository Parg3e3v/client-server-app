package com.parg3v.domain.use_cases.client

import com.parg3v.domain.common.DataStoreError
import com.parg3v.domain.common.Result
import com.parg3v.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SavePortClientAppUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(value: String): Flow<Result<String?, DataStoreError>> = flow {
        try {
            emit(Result.Loading())
            dataStoreRepository.savePortClient(value)
            val fieldValue = dataStoreRepository.getPortClient()
            emit(Result.Success(fieldValue))
        } catch (e: IOException) {
            emit(Result.Error(DataStoreError.BASIC))
        }
    }
}