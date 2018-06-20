package com.awsm_guys.mobileclicker.clicker.model.controller.localnetwork.poko

import com.fasterxml.jackson.annotation.JsonValue

enum class Header(@get:JsonValue val header: String) {
    CONNECT("CONNECT"),
    OK("OK"),
    SWITCH_PAGE("SWITCH_PAGE")
}