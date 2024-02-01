package http.forgot.model.request


import com.google.gson.annotations.SerializedName

data class ResetPasswordPayload(
    @SerializedName("identity")
    var identity: String?, // <string>
    @SerializedName("password")
    var password: String?, // <string>
    @SerializedName("password_confirmation")
    var passwordConfirmation: String?, // <string>
    @SerializedName("token")
    var token: String? // <string>
)