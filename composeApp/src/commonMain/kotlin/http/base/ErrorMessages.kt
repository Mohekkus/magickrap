package http.base

enum class ErrorMessages {
    SUCCESS_NO_DATA {
        override fun alias(): String = "api_success_no_data"
                    },
    FAILED {
        override fun alias(): String = "api_failure_no_data"
    };

    abstract fun alias(): String
}