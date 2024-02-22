package http.multidevice.model.response


import com.google.gson.annotations.SerializedName

data class ListedDeviceResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("meta")
    var meta: Meta?
) {
    data class Data(
        @SerializedName("attributes")
        var attributes: Attributes?,
        @SerializedName("current_login")
        var currentLogin: Int?, // 1
        @SerializedName("device_id")
        var deviceId: String?, // c5172f7ee835a5c4
        @SerializedName("expired")
        var expired: Long?, // 1709582917000
        @SerializedName("id")
        var id: String?, // 9ae67bf4-907b-4517-84e8-26fb98d9a25f
        @SerializedName("jti")
        var jti: String? // IMTniJ2wZsJBOKha
    ) {
        data class Attributes(
            @SerializedName("country")
            var country: String?, // United States
            @SerializedName("device_model")
            var deviceModel: String?, // Other
            @SerializedName("ip_address")
            var ipAddress: String?, // 54.86.50.139
            @SerializedName("is_primary")
            var isPrimary: Int?, // 0
            @SerializedName("mac_address")
            var macAddress: String?,
            @SerializedName("os_type")
            var osType: String?, // Other
            @SerializedName("timeout")
            var timeout: Int? // 1706954947
        )
    }

    data class Meta(
        @SerializedName("code")
        var code: Int?, // 200
        @SerializedName("message")
        var message: String?, // auth.profile.connected_device.success
        @SerializedName("status")
        var status: String? // success
    )
}