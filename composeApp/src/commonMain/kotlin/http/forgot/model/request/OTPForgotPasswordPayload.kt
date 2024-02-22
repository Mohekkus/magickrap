package http.forgot.model.request


import com.google.gson.annotations.SerializedName

data class OTPForgotPasswordPayload(
    @SerializedName("identity")
    var identity: String?, // <email>
    @SerializedName("token")
    var token: String? // <string>
)