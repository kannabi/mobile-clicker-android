package com.awsm_guys.mobileclicker.clicker.model.controller.lan.poko

data class ClickerMessage (
        var header: Header,
        var body: String,
        var features: MutableMap<String, String>
)

