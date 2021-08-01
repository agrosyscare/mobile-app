package com.example.appmonitor.model

import com.google.gson.annotations.SerializedName

data class Condition(
    val id: Int,
    val reading: Int,
    val status: String,
    @SerializedName("floor_id")
    val floorId: Int
)
