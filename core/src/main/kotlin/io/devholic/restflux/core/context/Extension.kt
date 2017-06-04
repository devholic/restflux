package io.devholic.restflux.core.context

import io.devholic.restflux.core.RestfluxContext
import io.devholic.restflux.core.response.*
import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable
import java.nio.charset.Charset
import java.util.*


private val restflux = "restflux"

inline fun <reified T> RequestContext.parseBody(charset: Charset = Charsets.UTF_8): Observable<T> =
    req.content
        .map { it.toString(charset) }
        .flatMap {
            body ->
            Observable.fromCallable {
                RestfluxContext.gson.fromJson<T>(body, T::class.java)
            }
        }

private fun HttpServerResponse<ByteBuf>.addDefaultHeader() {
    addDateHeader(Header.Date.value, Date())
    addHeader(Header.Server.value, "$restflux/${RestfluxContext.version}")
}

fun RequestContext.send(
    data: RestfluxResponse,
    contentType: ContentType = ContentType.JSON
): Observable<Void> =
    data.toJsonString().let {
        res.apply {
            status = data.statusCode
            addHeader(Header.ContentType.value, contentType.value)
            addHeader(Header.ContentLength.value, it.length)
            addDefaultHeader()
        }.writeStringAndFlushOnEach(Observable.just(it))
    }

fun RequestContext.send(
    data: RestfluxStatusResponse,
    contentType: ContentType = ContentType.TextPlain
): Observable<Void> =
    res.apply {
        status = data.statusCode
        addHeader(Header.ContentType.value, contentType.value)
        addHeader(Header.ContentLength.value, 0)
        addDefaultHeader()
    }.writeAndFlushOnEach(Observable.empty())
