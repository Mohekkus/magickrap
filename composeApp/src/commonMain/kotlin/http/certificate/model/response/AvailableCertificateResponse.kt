package http.certificate.model.response


import com.google.gson.annotations.SerializedName
import http.base.MetaPayload

data class AvailableCertificateResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: MetaPayload?
) {
    data class Data(
        @SerializedName("current_page")
        var currentPage: Int?, // 2
        @SerializedName("first_page_url")
        var firstPageUrl: String?, // https://api.auxonode.com/[end_point_url]?page=1
        @SerializedName("from")
        var from: Int?, // 16
        @SerializedName("items")
        var items: List<Item?>?,
        @SerializedName("las_page")
        var lasPage: Int?, // 1
        @SerializedName("last_page_url")
        var lastPageUrl: String?, // https://api.auxonode.com/[end_point_url]?page=34
        @SerializedName("next_page_url")
        var nextPageUrl: String?, // https://api.auxonode.com/[end_point_url]?page=3
        @SerializedName("path")
        var path: String?, // https://api.auxonode.com/[end_point_url]
        @SerializedName("per_page")
        var perPage: Int?, // 15
        @SerializedName("previous_page_url")
        var previousPageUrl: String?, // https://api.auxonode.com/[end_point_url]?page=1
        @SerializedName("to")
        var to: Int?, // 30
        @SerializedName("total")
        var total: Int? // 500
    ) {
        data class Item(
            @SerializedName("city")
            var city: String?, // Helsinki
            @SerializedName("continent")
            var continent: String?, // Europe
            @SerializedName("coordinates")
            var coordinates: Coordinates?,
            @SerializedName("country")
            var country: String?, // Finland
            @SerializedName("host_name")
            var hostName: String?, // fi1.auxonode.com
            @SerializedName("id")
            var id: String?, // 97cbd011-01c2-496d-8d9e-e6bf475929ed
            @SerializedName("ip")
            var ip: String?, // 25.200.36.50
            @SerializedName("name")
            var name: String?, // Finland-1
            @SerializedName("serviceable")
            var serviceable: Boolean?, // true
            @SerializedName("type")
            var type: String? // dynamic
        ) {
            data class Coordinates(
                @SerializedName("lat")
                var lat: Double?, // 12.788
                @SerializedName("lon")
                var lon: Double? // -12.788
            )
        }
    }
}