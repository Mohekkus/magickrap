package http.base

import com.google.gson.annotations.SerializedName


data class MetaPayload(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: String?
)