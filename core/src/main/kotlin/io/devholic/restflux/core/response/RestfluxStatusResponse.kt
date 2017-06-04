package io.devholic.restflux.core.response

import io.netty.handler.codec.http.HttpResponseStatus


data class RestfluxStatusResponse(
    val statusCode: HttpResponseStatus
)
