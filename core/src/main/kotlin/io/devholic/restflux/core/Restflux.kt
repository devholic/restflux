package io.devholic.restflux.core

import com.google.gson.Gson
import io.devholic.restflux.core.context.RequestContext
import io.devholic.restflux.core.context.send
import io.devholic.restflux.core.middleware.ContentTypeFilterMiddleware
import io.devholic.restflux.core.middleware.RestfluxMiddleware
import io.devholic.restflux.core.middleware.concat
import io.devholic.restflux.core.response.RestfluxStatusResponse
import io.devholic.restflux.core.route.Route
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import rx.Observable


class Restflux(
    val port: Int = 8080,
    gson: Gson = Gson(),
    val middlewares: List<RestfluxMiddleware> =
        listOf(
            ContentTypeFilterMiddleware()
        ),
    val routes: List<Route>
) {

    init {
        RestfluxContext.gson = gson
    }

    private val asciiArt = """
                           __  ______
           ________  _____/ /_/ __/ /_  ___  __
          / ___/ _ \/ ___/ __/ /_/ / / / / |/_/
         / /  /  __(__  ) /_/ __/ / /_/ />  <
        /_/   \___/____/\__/_/ /_/\__,_/_/|_|

"""
    private var server: HttpServer<ByteBuf, ByteBuf>? = null

    fun start() {
        print(asciiArt)
        server = HttpServer.newServer(port)
            .start {
                req, res ->
                Observable.just(RequestContext(req, res))
                    .compose(middlewares.concat())
                    .flatMap {
                        ctx ->
                        routes.find {
                            it.path.matches(req.decodedPath)
                        }?.let {
                            ctx.params = it.path.parseParam(req.decodedPath)
                            it.handleRequest(ctx)
                        } ?: ctx.send(RestfluxStatusResponse(HttpResponseStatus.NOT_FOUND))
                    }
            }
        server?.awaitShutdown()
    }

    fun shutdown() {
        server?.shutdown()
    }
}
