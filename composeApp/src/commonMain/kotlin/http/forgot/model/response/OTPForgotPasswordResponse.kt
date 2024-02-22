package http.forgot.model.response


import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class OTPForgotPasswordResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
) {
    data class Data(
        @SerializedName("token")
        var token: String? // 0294b651d5ba6984e81e0058db5ef8a58bd2c53fa89c8f7d101caeb095601294
    )
}