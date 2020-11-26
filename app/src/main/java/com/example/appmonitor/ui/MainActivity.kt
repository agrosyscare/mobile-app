package com.example.appmonitor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appmonitor.util.PreferenceHelper
import com.example.appmonitor.util.PreferenceHelper.get
import com.example.appmonitor.util.PreferenceHelper.set
import com.example.appmonitor.R
import com.example.appmonitor.io.ApiService
import com.example.appmonitor.model.LoginResponse
import com.example.appmonitor.util.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val snackBar by lazy {
        Snackbar.make(loginLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            token?.let { Log.d("FCMAgroSyscare", it) }

        })

        val preferences = PreferenceHelper.defaultPrefs(this)

        if (preferences["accessToken", ""].contains("."))
            goToMenuActivity()

        btnLogin.setOnClickListener {
            // Validar login con el servidor
            performLogin()
        }
    }

    // Metodos privados
    private fun performLogin() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            toast(getString(R.string.error_empty_credentials))
            return
        }
        val call = apiService.postLogin(email, password)

        call.enqueue(object: Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast(getString(R.string.error_login_response))
                        return
                    }
                    if (!loginResponse.error) {
                        createSessionPreference(loginResponse.accessToken)
                        toast(getString(R.string.welcome_message))
                        goToMenuActivity(true)
                    }
                } else {
                    toast(getString(R.string.error_invalid_credentials))
                }
            }
        })
    }

    private fun createSessionPreference(accessToken: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["accessToken"] = accessToken
    }

    private fun goToMenuActivity(isUserInput: Boolean = false) {
        val i = Intent(this, HomeActivity::class.java)

        if (isUserInput) {
            i.putExtra("store_token", true)
        }

        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        if (snackBar.isShown)
            super.onBackPressed()
        else
            snackBar.show()
    }
}