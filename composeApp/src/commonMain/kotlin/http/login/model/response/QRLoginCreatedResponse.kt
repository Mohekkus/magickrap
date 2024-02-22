package http.login.model.response


import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class QRLoginCreatedResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
) {
    data class Data(
        @SerializedName("accepted")
        var accepted: String?, // false
        @SerializedName("country")
        var country: String?, // Australia
        @SerializedName("device")
        var device: String?, // Mac
        @SerializedName("encrypted")
        var encrypted: String?, // eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9...
        @SerializedName("id")
        var id: String?, // 01GT9GD5W2A2487P43Q9ASPH38
        @SerializedName("ip")
        var ip: String?, // 186.75.174.31
        @SerializedName("os")
        var os: String?, // Mac OS X 10.6.2
        @SerializedName("timeout")
        var timeout: Long? // 1677507724
    )
}