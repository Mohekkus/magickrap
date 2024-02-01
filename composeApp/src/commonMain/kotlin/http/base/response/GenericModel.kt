package http.base.response


import com.google.gson.annotations.SerializedName

data class GenericModel(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: GenericMetaPayload?
) {
    class Data
}