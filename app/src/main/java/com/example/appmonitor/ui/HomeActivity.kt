package com.example.appmonitor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.appmonitor.R
import com.example.appmonitor.io.ApiService
import com.example.appmonitor.util.PreferenceHelper
import com.example.appmonitor.util.PreferenceHelper.get
import com.example.appmonitor.util.PreferenceHelper.set
import com.example.appmonitor.util.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val storeToken = intent.getBooleanExtra("store_token", false)
        if(storeToken) {
            storeToken()
        }

        btnViewGreenhouses.setOnClickListener {
            val i = Intent(this, GreenhouseActivity::class.java)
            startActivity(i)
        }

        btnLogout.setOnClickListener {
            performLogout()
            clearSessionPreference()

        }
    }

    private fun storeToken() {
        val accessToken = preferences["accessToken",""]
        val authHeader = "Bearer $accessToken"

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val deviceToken = task.result
            val call = deviceToken?.let { apiService.postToken(authHeader, it) }

            call?.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d(Companion.TAG, "Token registrado correctamente")
                    } else {
                        Log.d(Companion.TAG, "Hubo un problema al registrar el token")
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    toast(t.localizedMessage)
                }

            })
        })
    }

    // Metodos privados

    private fun performLogout() {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.postLogout("Bearer $accessToken")

        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()

                val i = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun clearSessionPreference() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["accessToken"] = ""
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}