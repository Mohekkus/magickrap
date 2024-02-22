package http.login.model.response


import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class OAuth2LoginResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
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
}