package com.example.appmonitor.model

import com.google.gson.annotations.SerializedName

data class Floor(
    val id: Int,
    @SerializedName("name")
    val nameSection: String,
    @SerializedName("plant_type")
    val plantingType: String,
    @SerializedName("greenhouse_id")
    val greenhouseId: Int
)