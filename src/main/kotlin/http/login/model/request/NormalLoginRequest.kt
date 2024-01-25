package http.login.model.request

import com.google.gson.annotations.SerializedName

data class NormalLoginRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("identity")
	val identity: String? = null
)
