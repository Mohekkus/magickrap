package http.login.model.request


import androidx.compose.ui.text.intl.Locale
import com.google.gson.annotations.SerializedName

data class QRLoginRequest(
    @SerializedName("country")
    var country: String = Locale.current.region, // ID
    @SerializedName("device_id")
    var deviceId: String?, // 01GT9HYDY9K6MCXTMS72SF3N18
    @SerializedName("device_model")
    var deviceModel: String?, // iPhone
    @SerializedName("ip_address")
    var ipAddress: String?, // 192.168.0.0
    @SerializedName("is_primary")
    var isPrimary: Int?, // 0
    @SerializedName("mac_address")
    var macAddress: String?, // 123.123.123
    @SerializedName("os_type")
    var osType: String? // mac os
)