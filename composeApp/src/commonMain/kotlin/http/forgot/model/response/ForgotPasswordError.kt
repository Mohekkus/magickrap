package http.forgot.model.response


import com.google.gson.annotations.SerializedName

data class ForgotPasswordError(
    @SerializedName("errors")
    var errors: Errors?,
    @SerializedName("message")
    var message: String? // The selected provider is invalid. (and 1 more error)
) {
    data class Errors(
        @SerializedName("provider")
        var provider: List<String?>?,
        @SerializedName("url")
        var url: List<String?>?
    )
}