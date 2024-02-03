package http.multidevice.model.request


import com.google.gson.annotations.SerializedName

data class AuthorizeDevicePayload(
    @SerializedName("authorize")
    var authorize: Boolean?, // true
    @SerializedName("encrypted")
    var encrypted: String? // <string>
)