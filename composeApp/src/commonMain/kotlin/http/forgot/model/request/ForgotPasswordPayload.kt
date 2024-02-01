package http.forgot.model.request


import com.google.gson.annotations.SerializedName

data class ForgotPasswordPayload(
    @SerializedName("identity")
    var identity: String?, // mohekkuslocker@gmail.com
    @SerializedName("provider")
    var provider: String? // mail
)