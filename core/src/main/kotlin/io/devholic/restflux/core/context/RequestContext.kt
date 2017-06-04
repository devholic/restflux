package io.devholic.restflux.core.context

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse


data class RequestContext(
    val req: HttpServerRequest<ByteBuf>,
    val res: HttpServerResponse<ByteBuf>,
    var params: Map<String, String> = emptyMap(),
    var paramEntity: Any? = null,
    var payload: HashMap<String, Any> = hashMapOf(),
    var isDone: Boolean = false
)
