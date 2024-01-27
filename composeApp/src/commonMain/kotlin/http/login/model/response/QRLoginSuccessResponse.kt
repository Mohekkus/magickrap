package http.login.model.response


import com.google.gson.annotations.SerializedName

data class QRLoginSuccessResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: Meta?
) {
    data class Data(
        @SerializedName("access_token")
        var accessToken: String?, // eyJ0eXAiOiJKVxxxxx...
        @SerializedName("exp")
        var exp: Int?, // 1666005484
        @SerializedName("expires_in")
        var expiresIn: Int?, // 3600
        @SerializedName("iat")
        var iat: Int?, // 1666001884
        @SerializedName("token_type")
        var tokenType: String? // bearer
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