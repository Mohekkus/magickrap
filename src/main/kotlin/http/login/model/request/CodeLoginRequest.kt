package http.login.model.request


import com.google.gson.annotations.SerializedName

data class CodeLoginRequest(
    @SerializedName("code")
    var code: String?
)