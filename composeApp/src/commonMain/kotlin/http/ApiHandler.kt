package http

import http.certificate.CertificateHandler
import http.login.LoginHandler

object ApiHandler {

    val authentication = LoginHandler.instance
    val certificate = CertificateHandler.instance

}