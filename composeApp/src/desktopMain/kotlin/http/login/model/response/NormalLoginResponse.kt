package http.login.model.response


import com.google.gson.annotations.SerializedName
import http.base.MetaPayload

data class NormalLoginResponse(
    @SerializedName("data")
    var `data`: NormalLoginData?,
    @SerializedName("meta")
    var meta: MetaPayload?
) {
    data class NormalLoginData(
        @SerializedName("access_token")
        var accessToken: String?,
        @SerializedName("email")
        var email: Any?,
        @SerializedName("exp")
        var exp: Int?,
        @SerializedName("expires_in")
        var expiresIn: Int?,
        @SerializedName("iat")
        var iat: Int?,
        @SerializedName("token_type")
        var tokenType: String?
    )
}