package com.example.appmonitor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appmonitor.R
import com.example.appmonitor.model.Floor
import kotlinx.android.synthetic.main.row_item_floor.view.*
import kotlinx.android.synthetic.main.row_item_greenhouse.view.*

class FloorAdapter(private val listener: FloorActivity) :
    RecyclerView.Adapter<FloorAdapter.ViewHolder>() {

    var floors = ArrayList<Floor>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(floor: Floor) =
            with(itemView) {
                tvFloorId.text = floor.id.toString()
                tvFloorName.text = floor.nameSection
                tvFloorPlantingType.text = floor.plantingType
                tvFloorGreenhouseId.text = floor.greenhouseId.toString()
            }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            val id: String = itemView.tvFloorId.text.toString()
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_floor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val floor = floors[position]
        holder.bind(floor)
    }

    override fun getItemCount(): Int {
        return floors.size
    }

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}