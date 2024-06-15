package com.parg3v.domain.common

sealed interface Error

enum class GestureLogError: Error {
    BASIC
}

enum class DataStoreError: Error {
    BASIC
}