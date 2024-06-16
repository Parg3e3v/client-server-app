package com.parg3v.domain.use_cases.common

import com.parg3v.domain.utils.getLocalIpAddress

class ProvideServerIpUseCase {
    operator fun invoke(): String = getLocalIpAddress() ?: ""
}