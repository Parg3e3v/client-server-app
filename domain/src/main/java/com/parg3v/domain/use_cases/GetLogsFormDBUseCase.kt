package com.parg3v.domain.use_cases

import com.parg3v.domain.common.GestureLogError
import com.parg3v.domain.common.Result
import com.parg3v.domain.model.GestureLog
import com.parg3v.domain.repository.GestureLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetLogsFormDBUseCase @Inject constructor(private val repository: GestureLogRepository) {
    operator fun invoke(): Flow<Result<List<GestureLog>, GestureLogError>> = flow {
        try {
            emit(Result.Loading())
            val result = repository.getAllLogs()
            emit(Result.Success(result))
        }catch (e: IOException){
            emit(Result.Error(GestureLogError.BASIC))
        }
    }
}