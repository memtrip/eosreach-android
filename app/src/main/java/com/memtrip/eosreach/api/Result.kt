package com.memtrip.eosreach.api

class Result<T, E : ApiError>(
    val response: T?,
    val errorKind: E? = null,
    val success: Boolean = errorKind == null
) {
    constructor(response: T?) : this(response, null)
    constructor(errorKind: E?) : this(null, errorKind)
}

interface ApiError