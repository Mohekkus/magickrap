package http.certificate.model.payload


import com.google.gson.annotations.SerializedName

public data class GeneratePayload(
    @SerializedName("protocol")
    var protocol: String?, // wireguard
    @SerializedName("server_id")
    var server_id: String? // wiw
)