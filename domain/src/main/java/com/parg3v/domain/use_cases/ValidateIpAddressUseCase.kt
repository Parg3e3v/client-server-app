package com.parg3v.domain.use_cases

class ValidateIpAddressUseCase{
    operator fun invoke(value: String): Boolean =
        Regex("((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\n")
            .matches(value)
}