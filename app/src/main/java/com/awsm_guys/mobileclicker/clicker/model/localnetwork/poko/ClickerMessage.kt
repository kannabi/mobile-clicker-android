package com.awsm_guys.mobileclicker.clicker.model.localnetwork.poko

data class ClickerMessage (
        var header: Header,
        var body: String,
        var features: MutableMap<String, String>
)

