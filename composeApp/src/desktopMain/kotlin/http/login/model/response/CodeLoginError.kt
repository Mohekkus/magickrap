package http.login.model.response


import com.google.gson.annotations.SerializedName
import http.base.MetaPayload

data class CodeLoginError(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: MetaPayload?
) {
    data class Data(
        @SerializedName("tips")
        var tips: String?
    )
}