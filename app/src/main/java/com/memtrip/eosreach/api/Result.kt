package com.memtrip.eosreach.api

class Result<T, E : ApiError>(
    val data: T?,
    val apiError: E? = null,
    val success: Boolean = apiError == null
) {
    constructor(response: T?) : this(response, null)
    constructor(errorKind: E?) : this(null, errorKind)
}

interface ApiError