package com.example.appmonitor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmonitor.R
import com.example.appmonitor.io.ApiService
import com.example.appmonitor.model.Greenhouse
import com.example.appmonitor.util.PreferenceHelper
import com.example.appmonitor.util.PreferenceHelper.get
import com.example.appmonitor.util.toast
import kotlinx.android.synthetic.main.activity_greenhouse.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GreenhouseActivity : AppCompatActivity(), GreenhouseAdapter.OnItemClickListener {

    private val apiService by lazy {
        ApiService.create()
    }
    
    private val preferences by lazy { 
        PreferenceHelper.defaultPrefs(this)
    }

    private val greenhouseAdapter = GreenhouseAdapter(this)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greenhouse)

        loadGreenhouses()

        rvGreenhouses.layoutManager = LinearLayoutManager(this) // GridLayoutManager
        rvGreenhouses.adapter = greenhouseAdapter
    }

    private fun loadGreenhouses() {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.getGreenhouses("Bearer $accessToken")

        call.enqueue(object: Callback<ArrayList<Greenhouse>> {
            override fun onResponse(
                call: Call<ArrayList<Greenhouse>>,
                response: Response<ArrayList<Greenhouse>>
            ) {
               if (response.isSuccessful) {
                   response.body()?.let {
                       greenhouseAdapter.greenhouses = it
                       greenhouseAdapter.notifyDataSetChanged()
                   }

               }
            }

            override fun onFailure(call: Call<ArrayList<Greenhouse>>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    override fun onItemClick(id: String) {
        val i = Intent(this, FloorActivity::class.java)
        i.putExtra("greenhouse_id", id)
        startActivity(i)
    }
}