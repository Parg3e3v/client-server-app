package com.parg3v.domain.use_cases.common

import com.parg3v.domain.common.config.ServerConfig

class ProvideServerIpUseCase {
    operator fun invoke(): String = ServerConfig.IP
}