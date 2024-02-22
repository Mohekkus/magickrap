package http.multidevice.model.request


import com.google.gson.annotations.SerializedName

data class RevokeDevicePayload(
    @SerializedName("connected_device_ids")
    var connectedDeviceIds: List<String?>?
)