package http.multidevice.model.response


import com.google.gson.annotations.SerializedName

data class GetAuthenticationCodeResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("meta")
    var meta: Meta?
) {
    data class Data(
        @SerializedName("code")
        var code: String?, // 756642
        @SerializedName("encrypted")
        var encrypted: String?, // eyJpdiI6ImMwcnlDT0x5VnJGTGo0L1E3MzZGZGc9PSIsInZhbHVlIjoiWVU0eUJIbTdSN1EwdnNZS2Izd0xDbUxPUmdIK0Nva2M5Zmtacy9rYTh3T0VNejBOU1ovNHVxUnlXR2lBZEVsWSIsIm1hYyI6ImJkYTVjNTU3NDBkOTkxMGFjY2FhYTZiNjk1OTZlNjI0MzlmYTY3ZGFjMzFmMmE0NTZjOWM5ODdiNGQ2MTYwOTYiLCJ0YWciOiIifQ==
        @SerializedName("timeout")
        var timeout: Timeout?
    ) {
        data class Timeout(
            @SerializedName("in")
            var inX: String?, // 180 seconds
            @SerializedName("unixstamp")
            var unixstamp: Int? // 1706954425
        )
    }

    data class Meta(
        @SerializedName("code")
        var code: Int?, // 200
        @SerializedName("message")
        var message: String?, // response.success
        @SerializedName("status")
        var status: String? // success
    )
}