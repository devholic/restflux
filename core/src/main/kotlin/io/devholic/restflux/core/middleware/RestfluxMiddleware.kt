package io.devholic.restflux.core.middleware

import io.devholic.restflux.core.context.RequestContext
import rx.Observable


abstract class RestfluxMiddleware {

    abstract fun buildMiddlewareObservable(ctx: RequestContext): Observable<RequestContext>

    fun Observable<Void>.done(ctx: RequestContext): Observable<RequestContext> =
        flatMap {
            Observable.fromCallable { ctx.apply { isDone = true } }
        }
}
