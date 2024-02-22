package http.base.response

enum class ErrorMessages {
    SUCCESS_NO_DATA {
        override fun value(): String = "Succeeded, but no data available"
                    },
    FAILED {
        override fun value(): String = "There is no error messages available"
    };

    abstract fun value(): String
}