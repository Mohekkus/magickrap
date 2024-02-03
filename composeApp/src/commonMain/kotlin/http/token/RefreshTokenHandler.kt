package http.token

import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.token.model.RefreshTokenResponse

class RefreshTokenHandler {

    companion object {
        val instance = RefreshTokenHandler()
        private val refresh = ClientModule.instance.refresh
    }

    fun refreshToken(
        token: String,
        onFailure: (String) -> Unit,
        onSuccess: (RefreshTokenResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                refresh.refreshToken(token)
            }
        ) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure("Succeeded, but no data available")
                    else
                        Gson().fromJson(it.data.toString(), RefreshTokenResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: "There is no error messages available")
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: "There is no error messages available")
                        }

            }
        }
    }
}