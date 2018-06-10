package com.awsm_guys.mobileclicker.udp.poko

data class ClickerMessage (
        var header: Header,
        var body: String,
        var features: MutableMap<String, String>
)

