package io.devholic.restflux.core.path


class Path(path: String) {

    private val pathRegex: Regex by lazy { mapPathToRegex(path) }
    private val paramIndex: HashMap<Int, String> = hashMapOf()

    fun matches(requestPath: String): Boolean = requestPath.matches(pathRegex)

    fun parseParam(requestPath: String): Map<String, String> =
        pathRegex.find(requestPath)?.groups?.let {
            hashMapOf<String, String>().apply {
                paramIndex.forEach {
                    groupIdx, key ->
                    it[groupIdx]?.let { put(key, it.value) }
                }
            }
        } ?: mapOf<String, String>()

    private fun mapPathToRegex(path: String): Regex =
        path.split("/")
            .map {
                param ->
                if (param.startsWith(":")) {
                    "(\\w+)".let {
                        paramIndex.run { put(size + 1, param.replaceFirst(":", "")) }
                        it
                    }
                } else param
            }.joinToString(separator = "/")
            .toRegex()
}
