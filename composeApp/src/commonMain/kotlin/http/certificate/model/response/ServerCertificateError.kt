package http.certificate.model.response


import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class ServerCertificateError(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
) {
    class Data
}