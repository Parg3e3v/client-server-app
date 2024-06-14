package com.parg3v.domain.use_cases

import com.parg3v.domain.common.config.ServerConfig

class ProvideServerIpUseCase {
    operator fun invoke(): String = ServerConfig.IP
}