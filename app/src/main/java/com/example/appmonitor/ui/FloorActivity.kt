package com.example.appmonitor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmonitor.R
import com.example.appmonitor.io.ApiService
import com.example.appmonitor.model.Floor
import com.example.appmonitor.util.PreferenceHelper
import com.example.appmonitor.util.PreferenceHelper.get
import com.example.appmonitor.util.toast
import kotlinx.android.synthetic.main.activity_floor.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FloorActivity : AppCompatActivity(), FloorAdapter.OnItemClickListener {

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val floorAdapter = FloorAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor)

        val greenhouseId = intent.getStringExtra("greenhouse_id")

        greenhouseId?.let { loadFloors(it) }

        rvFloors.layoutManager = LinearLayoutManager(this) // GridLayoutManager
        rvFloors.adapter = floorAdapter
    }

    private fun loadFloors(id: String) {
        val accessToken = preferences["accessToken", ""]
        val call = apiService.getFloors("Bearer $accessToken", id)

        call.enqueue(object: Callback<ArrayList<Floor>> {
            override fun onResponse(
                call: Call<ArrayList<Floor>>,
                response: Response<ArrayList<Floor>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        floorAdapter.floors = it
                        floorAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Floor>>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    override fun onItemClick(id: String) {
        val i = Intent(this, ConditionActivity::class.java)
        i.putExtra("greenhouse_section_id", id)
        startActivity(i)
    }
}