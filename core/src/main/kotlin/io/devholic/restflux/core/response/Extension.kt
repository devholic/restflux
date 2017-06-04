package io.devholic.restflux.core.response

import io.devholic.restflux.core.RestfluxContext


fun RestfluxResponse.toJsonString(): String = RestfluxContext.gson.toJson(data)
