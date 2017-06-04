package io.devholic.restflux.core.middleware

import io.devholic.restflux.core.context.RequestContext
import io.devholic.restflux.core.context.send
import io.devholic.restflux.core.response.ContentType
import io.devholic.restflux.core.response.Header
import io.devholic.restflux.core.response.RestfluxStatusResponse
import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable


class ContentTypeFilterMiddleware(
    private val contentTypeFilter: List<ContentType> = listOf(ContentType.JSON)
) : RestfluxMiddleware() {

    override fun buildMiddlewareObservable(ctx: RequestContext): Observable<RequestContext> =
        Observable.fromCallable {
            contentTypeFilter
                .filter {
                    it.value == ctx.req.getHeader(Header.ContentType.value)?.toLowerCase()
                }.isNotEmpty()
        }.flatMap {
            if (it) {
                Observable.just(ctx)
            } else ctx.send(RestfluxStatusResponse(HttpResponseStatus.BAD_REQUEST)).done(ctx)
        }
}
