package vpn

import CertificateDocument
import com.google.gson.Gson
import java.util.*

class VpnCertificate {

    companion object {
        val instance = VpnCertificate()
    }

    private fun setOvpn(certificate: CertificateDocument): String {
        certificate.apply {
            println(Gson().toJson(certificate))

            return StringJoiner("\n")
                .add(config?.common?.joinToString("\n"))
                .add("compress lz4\n" +
                        "comp-lzo no")
                .add("key-direction ${config?.ca?.keyDirection}")

                .add("<ca>")
                .add("${config?.ca?.cert}")
                .add("</ca>")

                .add("<cert>")
                .add("${client?.cert}")
                .add("</cert>")

                .add("<key>")
                .add("${client?.key}")
                .add("</key>")

                .add("<tls-auth>")
                .add("${config?.tls}")
                .add("</tls-auth>")

                .toString()
                .replace("?", "\n")
        }
    }

    private fun setWireguard(certificate: CertificateDocument): String {
        certificate.apply {
            return StringJoiner("\n")
                .apply {
                    add("[Interface]")
                    client?.apply {
                        add("Address = $address")
                        add("PrivateKey = $privateKey")
                        add("Port = $port")
                    }

                    add("[Peer]")
                    config?.apply {
                        add("AllowedIPs = $allowedIp")
                        add("PublicKey = $publicKey")
                        add("PresharedKey = $presharedKey")
                        add("Endpoint = $endpoint")
                    }
                }
                .toString()
        }
    }
}