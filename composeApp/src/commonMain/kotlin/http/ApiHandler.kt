package http

import http.certificate.CertificateHandler
import http.forgot.ForgotPasswordHandler
import http.login.LoginHandler
import http.logout.LogoutHandler
import http.register.RegisterHandler
import http.token.RefreshTokenHandler

object ApiHandler {

    val authentication = LoginHandler.instance
    val logout = LogoutHandler.instance
    val certificate = CertificateHandler.instance
    val forgot = ForgotPasswordHandler.instance
    val token = RefreshTokenHandler.instance
    val register = RegisterHandler.instance
}