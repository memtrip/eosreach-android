package com.memtrip.eosreach.api.stub

class StubMatcher(
    val rootUrl: String,
    val urlMatcher: Regex,
    val bodyMatcher: String? = null
)