package http.login.model.request


import com.google.gson.annotations.SerializedName

data class CodeLoginPayload(
    @SerializedName("code")
    var code: String?
)