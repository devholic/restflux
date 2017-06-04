package io.devholic.restflux.core.response

import io.netty.handler.codec.http.HttpResponseStatus


data class RestfluxResponse(
    val statusCode: HttpResponseStatus,
    val data: Any
)
