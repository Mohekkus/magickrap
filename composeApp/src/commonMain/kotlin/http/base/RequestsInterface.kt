package http.base

import http.certificate.CertificateRequests
import http.login.LoginRequests

object RequestsInterface {

    val login = LoginRequests.instance
    val certificate = CertificateRequests.instance
}