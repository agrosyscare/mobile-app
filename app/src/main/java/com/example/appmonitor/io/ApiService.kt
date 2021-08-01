package com.example.appmonitor.io

import com.example.appmonitor.model.Condition
import com.example.appmonitor.model.Floor
import com.example.appmonitor.model.Greenhouse
import com.example.appmonitor.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Manejo de sesiones
    @POST("login")
    fun postLogin(
        @Query("email") email: String,
        @Query("password") password: String):
            Call<LoginResponse>

    @GET("logout")
    fun postLogout(@Header("Authorization" ) authHeader: String): Call<Void>

    // Registro device_token
    @POST("fcm/token")
    fun postToken(
        @Header("Authorization") authHeader: String,
        @Query("device_token") token: String):
            Call<Void>

    // Lista de invernaderos
    @GET("greenhouses")
    fun getGreenhouses(@Header("Authorization" ) authHeader: String): Call<ArrayList<Greenhouse>>

    // Lista de canchas
    @GET("floors")
    fun getFloors(
        @Header("Authorization" ) authHeader: String,
        @Query("greenhouse_id") id: String):
            Call<ArrayList<Floor>>

    // Lectura de condiciones ambientales
    @GET("temperature_readings")
    fun getTemperatures(
        @Header("Authorization") authHeader: String,
        @Query("floor_id") id: String
    ):
            Call<Condition>

    @GET("humidity_readings")
    fun getEnvHumidities(
        @Header("Authorization") authHeader: String,
        @Query("floor_id") id: String
    ):
            Call<Condition>

    @GET("root_moisture_readings")
    fun getRetHumidities(
        @Header("Authorization") authHeader: String,
        @Query("floor_id") id: String
    ):
            Call<Condition>

    companion object Factory {
        private const val BASE_URL = "http://172.18.197.158:3000/api/v1/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

