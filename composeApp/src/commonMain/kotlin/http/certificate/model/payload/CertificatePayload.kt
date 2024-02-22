package http.certificate.model.payload


import com.google.gson.annotations.SerializedName

data class CertificatePayload(
    @SerializedName("filter[server_city]")
    var filterserverCity: String? = null, // Helsinki
    @SerializedName("filter[server_continent]")
    var filterserverContinent: String? = null, // EU
    @SerializedName("filter[server_country]")
    var filterserverCountry: String? = null, // Finland
    @SerializedName("filter[server_host_name]")
    var filterserverHostName: String? = null, // fi1.auxonode.com
    @SerializedName("filter[server_id]")
    var filterserverId: String? = null, // 9868b029-078b-40f7-a1f9-ec80e08c22cc
    @SerializedName("filter[server_ip]")
    var filterserverIp: String? = null, // 95.217.118.194
    @SerializedName("filter[server_name]")
    var filterserverName: String? = null, // Finland-1
    @SerializedName("filter[server_type]")
    var filterserverType: String? = null, // dynamic
    @SerializedName("page")
    var page: Int? = null, // 1
    @SerializedName("perPage")
    var perPage: Int? = null, // 15
    @SerializedName("sort")
    var sort: String? = null // protocols
)