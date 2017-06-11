package io.devholic.restflux.core.middleware

import io.devholic.restflux.core.context.RequestContext
import rx.Observable


fun List<RestfluxMiddleware>.concat(): (Observable<RequestContext>) -> Observable<RequestContext> =
    { this@concat.fold(it, { acc, middleware -> acc.applyMiddleware(middleware) }) }

fun Observable<RequestContext>.applyMiddleware(middleware: RestfluxMiddleware): Observable<RequestContext> =
    Observable.Transformer<RequestContext, RequestContext> {
        it.flatMap {
            middleware.buildMiddlewareObservable(it)
        }.filter { !it.isDone }
    }.call(this)
