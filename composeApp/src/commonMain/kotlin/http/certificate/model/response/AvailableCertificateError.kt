package http.certificate.model.response


import com.google.gson.annotations.SerializedName
import http.base.MetaPayload

data class AvailableCertificateError(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: MetaPayload?
) {
    class Data
}