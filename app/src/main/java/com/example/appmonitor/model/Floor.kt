package com.example.appmonitor.model

import com.google.gson.annotations.SerializedName

data class Floor(
    val id: Int,
    @SerializedName("name_section")
    val nameSection: String,
    @SerializedName("planting_type")
    val plantingType: String,
    @SerializedName("greenhouse_id")
    val greenhouseId: Int
)