package http.base.response

import com.google.gson.annotations.SerializedName


data class GenericMetaPayload(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: String?
)