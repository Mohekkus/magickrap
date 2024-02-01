import com.google.gson.annotations.SerializedName
import http.base.response.GenericMetaPayload

data class GeneratedCertificateResponse(
    @field:SerializedName("meta")
    val meta: GenericMetaPayload,

    @field:SerializedName("data")
    val data: GeneratedCertificateData?
)

data class GeneratedCertificateData(
    @field:SerializedName("protocols")
    val protocols: String,

    @field:SerializedName("expiration")
    val expiration: Long?,

    @field:SerializedName("last_connected")
    val lastConnected: String?,

    @field:SerializedName("document")
    val document: CertificateDocument?
)

data class CertificateDocument(
    @field:SerializedName("client")
    val client: CertificateClient?,

    @field:SerializedName("config")
    val config: CertificateConfig?
)

data class CertificateClient(
    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("key")
    val key: String?,

    @field:SerializedName("cert")
    val cert: String?,

    @field:SerializedName("dns")
    val dns: String?,

    @field:SerializedName("port")
    val port: String?,

    @field:SerializedName("address")
    val address: String?,

    @field:SerializedName("private_key")
    val privateKey: String?
)

data class CertificateConfig(
    @field:SerializedName("ca")
    val ca: CertificateCA?,

    @field:SerializedName("tls")
    val tls: String?,

    @field:SerializedName("tcp6")
    val tcp6: String?,

    @field:SerializedName("udp6")
    val udp6: String?,

    @field:SerializedName("common")
    val common: List<String>?,

    @field:SerializedName("endpoint")
    val endpoint: String?,

    @field:SerializedName("allowed_ip")
    val allowedIp: String?,

    @field:SerializedName("public_key")
    val publicKey: String?,

    @field:SerializedName("preshared_key")
    val presharedKey: String?
)

data class CertificateCA(
    @field:SerializedName("cert")
    val cert: String?,

    @field:SerializedName("key-direction")
    val keyDirection: Int?
)
