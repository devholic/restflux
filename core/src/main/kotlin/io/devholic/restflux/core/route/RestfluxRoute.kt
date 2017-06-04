package io.devholic.restflux.core.route

import io.devholic.restflux.core.context.RequestContext
import io.devholic.restflux.core.context.send
import io.devholic.restflux.core.middleware.RestfluxMiddleware
import io.devholic.restflux.core.middleware.concat
import io.devholic.restflux.core.path.Path
import io.devholic.restflux.core.response.RestfluxStatusResponse
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable


abstract class RestfluxRoute(val path: Path, val middlewares: List<RestfluxMiddleware> = emptyList()) {

    open fun handleRequest(ctx: RequestContext): Observable<Void> =
        Observable.just(ctx)
            .compose(middlewares.concat())
            .flatMap {
                when (it.req.httpMethod) {
                    HttpMethod.OPTIONS -> options(it)
                    HttpMethod.GET -> get(it)
                    HttpMethod.HEAD -> head(it)
                    HttpMethod.POST -> post(it)
                    HttpMethod.PUT -> put(it)
                    HttpMethod.PATCH -> patch(it)
                    HttpMethod.DELETE -> delete(it)
                    HttpMethod.TRACE -> trace(it)
                    HttpMethod.CONNECT -> connect(it)
                    else -> onMethodNotImplemented(it)
                }
            }

    open fun options(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun get(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun head(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun post(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun put(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun patch(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun delete(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun trace(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    open fun connect(ctx: RequestContext): Observable<Void> =
        onMethodNotImplemented(ctx)

    private fun onMethodNotImplemented(ctx: RequestContext): Observable<Void> =
        ctx.send(RestfluxStatusResponse(HttpResponseStatus.METHOD_NOT_ALLOWED))
}
