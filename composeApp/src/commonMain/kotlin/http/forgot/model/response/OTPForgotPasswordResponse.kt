package http.forgot.model.response


import com.google.gson.annotations.SerializedName

data class OTPForgotPasswordResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: Meta?
) {
    data class Data(
        @SerializedName("token")
        var token: String? // 0294b651d5ba6984e81e0058db5ef8a58bd2c53fa89c8f7d101caeb095601294
    )

    data class Meta(
        @SerializedName("code")
        var code: Int?, // 200
        @SerializedName("message")
        var message: String?, // operation successfully
        @SerializedName("status")
        var status: String? // success
    )
}