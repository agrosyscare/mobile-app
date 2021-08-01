package com.example.appmonitor.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmonitor.R
import com.example.appmonitor.io.ApiService
import com.example.appmonitor.model.Condition
import com.example.appmonitor.util.PreferenceHelper
import com.example.appmonitor.util.PreferenceHelper.get
import com.example.appmonitor.util.toast
import kotlinx.android.synthetic.main.activity_condition.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionActivity : AppCompatActivity() {

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition)

        val greenhouseSectionId = intent.getStringExtra("floor_id")

        greenhouseSectionId?.let { readTemperatures(it) }
        greenhouseSectionId?.let { readEnvironmentalHumidities(it) }
        greenhouseSectionId?.let { readReticularHumidities(it) }

    }

    //Metodos privados
    private fun readTemperatures(id: String) {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.getTemperatures("Bearer $accessToken", id)

        call.enqueue(object: Callback<Condition> {
            override fun onResponse(call: Call<Condition>, response: Response<Condition>) {
                if (response.isSuccessful){
                    tvTemperatureValue.text = response.body()?.reading.toString()+"°C"
                    tvTemperatureStatus.text = response.body()?.status
                } else {
                    toast(getString(R.string.error_reading))
                }
            }

            override fun onFailure(call: Call<Condition>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }

    private fun readEnvironmentalHumidities(id: String) {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.getEnvHumidities("Bearer $accessToken", id)

        call.enqueue(object: Callback<Condition> {
            override fun onResponse(call: Call<Condition>, response: Response<Condition>) {
                if (response.isSuccessful){
                    tvHAValue.text = response.body()?.reading.toString()+"%"
                    tvHAStatus.text = response.body()?.status
                } else {
                    toast(getString(R.string.error_reading))
                }
            }

            override fun onFailure(call: Call<Condition>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }

    private fun readReticularHumidities(id: String) {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.getRetHumidities("Bearer $accessToken", id)

        call.enqueue(object: Callback<Condition> {
            override fun onResponse(call: Call<Condition>, response: Response<Condition>) {
                if (response.isSuccessful){
                    tvHRValue.text = response.body()?.reading.toString()+"%"
                    tvHRStatus.text = response.body()?.status
                } else {
                    toast(getString(R.string.error_reading))
                }
            }

            override fun onFailure(call: Call<Condition>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }

}