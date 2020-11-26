package com.example.appmonitor.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val error: Boolean,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_at")
    val expiresAt: String
)