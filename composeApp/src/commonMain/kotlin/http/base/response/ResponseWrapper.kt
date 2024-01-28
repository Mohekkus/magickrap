package http.base.response


data class ResponseWrapper<out T>(
    val status: ResponseStatus,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T?): ResponseWrapper<T> {
            return ResponseWrapper(ResponseStatus.SUCCESS, data, null)
        }

        fun <T> error(data: T? = null, message: String): ResponseWrapper<T> {
            return ResponseWrapper(ResponseStatus.ERROR, data, message)
        }
    }
}