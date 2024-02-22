package http.login.model.response


import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class NormalLoginError(
    @SerializedName("data")
    var `data`: List<String?>?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
)