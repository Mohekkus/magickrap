package http.multidevice.model.response


import com.google.gson.annotations.SerializedName

data class AuthorizeDeviceResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: Meta?
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
        var timeout: Int? // 1677507724
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