package http.certificate.model.payload


import com.google.gson.annotations.SerializedName

data class AvailableServerPayload(
    @SerializedName("filter[city]")
    var filtercity: String? = null, // Helsinki
    @SerializedName("filter[continent]")
    var filtercontinent: String? = null, // EU
    @SerializedName("filter[country]")
    var filtercountry: String? = null, // Finland
    @SerializedName("filter[host_name]")
    var filterhostName: String? = null, // fi1.auxonode.com
    @SerializedName("filter[id]")
    var filterid: String? = null, // 9868b029-078b-40f7-a1f9-ec80e08c22cc
    @SerializedName("filter[ip]")
    var filterip: String? = null, // 95.217.118.194
    @SerializedName("filter[name]")
    var filtername: String? = null, // Finland-1
    @SerializedName("filter[serviceable]")
    var filterserviceable: String? = null, // true
    @SerializedName("filter[type]")
    var filtertype: String? = null, // dynamic
    @SerializedName("page")
    var page: Int? = null, // 1
    @SerializedName("perPage")
    var perPage: Int? = null, // 15
    @SerializedName("sort")
    var sort: String? = null // name
)