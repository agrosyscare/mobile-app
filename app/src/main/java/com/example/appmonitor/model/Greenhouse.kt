package com.example.appmonitor.model

import com.google.gson.annotations.SerializedName

data class Greenhouse(
    val id: Int,
    @SerializedName("name")
    val nameGreenhouse: String
)