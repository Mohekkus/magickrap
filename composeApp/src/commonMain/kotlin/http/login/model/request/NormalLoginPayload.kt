package http.login.model.request

import com.google.gson.annotations.SerializedName

data class NormalLoginPayload(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("identity")
	val identity: String? = null
)
